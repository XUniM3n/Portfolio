package com.market.application;

import com.google.gson.Gson;
import com.market.util.BlockioWebsocket;
import com.market.util.OrdersRegularCheck;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.market")
@EnableJpaRepositories(basePackages = "com.market.repository")
@EntityScan(basePackages = "com.market.model")
//@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class Application {

    private static BlockioWebsocket blockioWebsocket;
    private static OrdersRegularCheck ordersRegularCheck;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
//        blockioWebsocket = context.getBean(BlockioWebsocket.class);
//        blockioWebsocket.connect();
//        MoneySyncService moneySyncService = context.getBean(MoneySyncService.class);
//        moneySyncService.updateTransactions();
        OrdersRegularCheck ordersRegularCheck = context.getBean(OrdersRegularCheck.class);
        ordersRegularCheck.update();
        ordersRegularCheck.enableRegulularUpdate();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
