package com.market.util;

import com.market.model.Order;
import com.market.model.OrderStatus;
import com.market.model.User;
import com.market.repository.OrderRepository;
import com.market.repository.UserRepository;
import com.market.service.implementation.MoneyServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class OrdersRegularCheck {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderUpdater goalUpdater;
    private final MoneyServiceImpl moneySerivce;

    // One hour
    private long delay = 24 * 60 * 60 * 1000;

    @Autowired
    public OrdersRegularCheck(UserRepository userRepository, OrderRepository orderRepository, MoneyServiceImpl moneySerivce) {
        this.userRepository = userRepository;
        goalUpdater = new OrderUpdater(delay);
        this.orderRepository = orderRepository;
        this.moneySerivce = moneySerivce;
    }

    public void disableRegularUpdate() {
        if (goalUpdater != null) {
            goalUpdater.stop();
        }
    }

    public void enableRegulularUpdate() {
        // Start update at the beginning of the next hour.
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Timer timer = new Timer();
        timer.schedule(goalUpdater, calendar.getTime());
    }

    public void update() {
        goalUpdater.update();
    }

    private class OrderUpdater extends TimerTask {

        @Getter
        @Setter
        private long delay;
        private boolean stop = false;

        public OrderUpdater(long delay) {
            this.delay = delay;
        }

        private void stop() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                update();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    System.err.println("Order update checker interrupted");
                }
            }
        }

        public void update() {
            Calendar sixtyDaysAgoCalendar = Calendar.getInstance();
            sixtyDaysAgoCalendar.add(Calendar.HOUR, 24 * 60);
            Date sixtyDaysAgoDate = sixtyDaysAgoCalendar.getTime();
            for (Order order : orderRepository.findAllByStatus(OrderStatus.SHIPPED)) {
                if (order.getStatus().equals(OrderStatus.SHIPPED) && order.getDateShip().compareTo(sixtyDaysAgoDate) < 0) {
                    order.setStatus(OrderStatus.COMPLETED);
                    User seller = order.getSeller();
                    seller.setBalance(seller.getBalance().add(order.getPrice()));
                    moneySerivce.sendMoney(order.getBuyer(), seller.getWallet().getAddress(), order.getPrice());
                    orderRepository.saveAndFlush(order);
                    userRepository.saveAndFlush(seller);
                }
            }
        }
    }
}
