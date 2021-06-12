package com.market.controller;

import com.market.model.MoneyHistoryEntry;
import com.market.model.Order;
import com.market.model.OrderStatus;
import com.market.model.User;
import com.market.repository.MoneyHistoryEntryRepository;
import com.market.repository.OrderRepository;
import com.market.repository.UserRepository;
import com.market.service.AuthenticationService;
import com.market.service.implementation.MoneyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    private final AuthenticationService authenticationService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MoneyHistoryEntryRepository moneyHistoryEntryRepositoryжжжжж;
    private final MoneyServiceImpl moneyService;

    private final User admin;

    @Autowired
    public OrdersController(AuthenticationService authenticationService, OrderRepository orderRepository,
                            UserRepository userRepository, MoneyHistoryEntryRepository moneyHistoryEntryRepository,
                            MoneyServiceImpl moneyService) {
        this.authenticationService = authenticationService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.moneyService = moneyService;
        this.moneyHistoryEntryRepositoryжжжжж = moneyHistoryEntryRepository;
        admin = userRepository.findAdmin();
    }

    @GetMapping(value = {"", "/"})
    public String orders(Model model, Authentication authentication) {
        User user = authenticationService.getUserByAuthentication(authentication);

        model.addAttribute("user", user);
        model.addAttribute("ordersBuy", user.getBOrders().stream()
                .filter(o -> o.getStatus().equals(OrderStatus.OPEN) || o.getStatus().equals(OrderStatus.MONEY_SENT)
                        || o.getStatus().equals(OrderStatus.SHIPPED)).collect(Collectors.toList()));
        model.addAttribute("ordersSell", user.getSOrders().stream()
                .filter(o -> o.getStatus().equals(OrderStatus.OPEN) || o.getStatus().equals(OrderStatus.MONEY_SENT)
                        || o.getStatus().equals(OrderStatus.SHIPPED)).collect(Collectors.toList()));
        return "orders_list_page";
    }

    @PostMapping("/send")
    public String sendMoney(Authentication authentication, @RequestParam(value = "id") Long id, RedirectAttributes attributes) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            attributes.addFlashAttribute("error", "No order no with id found");
            return "redirect:/orders";
        }
        Order order = orderOptional.get();

        User user = authenticationService.getUserByAuthentication(authentication);
        if (!user.getBOrders().contains(order)) {
            attributes.addFlashAttribute("error", "You don't have order with this id");
            return "redirect:/orders";
        }

        BigDecimal price = order.getPrice();
        if (user.getBalance().compareTo(price) < 0) {
            attributes.addFlashAttribute("error", "Not enough money");
            return "redirect:/orders";
        }

        order.setStatus(OrderStatus.MONEY_SENT);
        orderRepository.saveAndFlush(order);

        MoneyHistoryEntry moneyHistoryEntry = MoneyHistoryEntry.builder()
                .sender(user).receiver(admin).amount(order.getPrice()).build();
        moneyHistoryEntryRepositoryжжжжж.saveAndFlush(moneyHistoryEntry);

        user.setBalance(user.getBalance().subtract(price));
        userRepository.saveAndFlush(user);
        attributes.addFlashAttribute("success", "Successfully deposited money, seller will " +
                "receive it after order completion");
        return "redirect:/orders";
    }

    @PostMapping("/ship")
    public String ship(Authentication authentication, @RequestParam(value = "id") Long id, RedirectAttributes attributes) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            attributes.addFlashAttribute("error", "No order no with id found");
            return "redirect:/orders";
        }
        Order order = orderOptional.get();

        User user = authenticationService.getUserByAuthentication(authentication);
        if (!user.getBOrders().contains(order)) {
            attributes.addFlashAttribute("error", "You don't have order with this id");
            return "redirect:/orders";
        }

        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.saveAndFlush(order);
        attributes.addFlashAttribute("success", "Now waiting util buyer confirm product delivery");
        return "redirect:/orders";
    }

    @PostMapping("/complete")
    public String completeOrder(Authentication authentication, @RequestParam(value = "id") Long id, RedirectAttributes attributes) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            attributes.addFlashAttribute("error", "No order no with id found");
            return "redirect:/orders";
        }
        Order order = orderOptional.get();
        if (!order.getStatus().equals(OrderStatus.SHIPPED)) {
            attributes.addFlashAttribute("error", "Product is not yet delivered");
            return "redirect:/orders";
        }

        User user = authenticationService.getUserByAuthentication(authentication);
        if (!user.getBOrders().contains(order)) {
            attributes.addFlashAttribute("error", "You don't have order with this id");
            return "redirect:/orders";
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.saveAndFlush(order);

        MoneyHistoryEntry moneyHistoryEntry = MoneyHistoryEntry.builder()
                .sender(admin).receiver(user).amount(order.getPrice()).build();
        moneyHistoryEntryRepositoryжжжжж.saveAndFlush(moneyHistoryEntry);

        BigDecimal price = order.getPrice();
        User seller = order.getSeller();
        moneyService.sendMoney(user, seller.getWallet().getAddress(), order.getPrice());
        userRepository.saveAndFlush(seller);

        attributes.addFlashAttribute("success", "Order completed, seller got his money");
        return "redirect:/orders";
    }

    @PostMapping("/cancel")
    public String cancelOrder(Authentication authentication, @RequestParam(value = "id") Long id, RedirectAttributes attributes) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            attributes.addFlashAttribute("error", "No order no with id found");
            return "redirect:/orders";
        }
        Order order = orderOptional.get();

        User user = authenticationService.getUserByAuthentication(authentication);
        if (!user.getBOrders().contains(order)) {
            attributes.addFlashAttribute("error", "You don't have order with this id");
            return "redirect:/orders";
        }

        // Return money to buyer if seller canceled order.
        if (user.getId().equals(order.getSeller().getId())) {
            if (order.getStatus().equals(OrderStatus.MONEY_SENT)) {
                User buyer = order.getBuyer();
                buyer.setBalance(buyer.getBalance());
                userRepository.saveAndFlush(buyer);
            }
        }
        order.setStatus(OrderStatus.CANCELLED);
        attributes.addFlashAttribute("success", "Order was successfully removed");
        return "redirect:/orders";
    }
}
