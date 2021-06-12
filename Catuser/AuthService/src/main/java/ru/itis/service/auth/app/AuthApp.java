package ru.itis.service.auth.app;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.ClientResources;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.service.auth.config.RedisConfig;

@SpringBootApplication
@ComponentScan("ru.itis.service.auth")
@EnableJpaRepositories(basePackages = "ru.itis.service.auth.repository")
@EntityScan(basePackages = "ru.itis.service.auth.model")
@RestController
@EnableEurekaClient
@EnableDiscoveryClient
public class AuthApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthApp.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    @Bean(destroyMethod = "shutdown")
    RedisClient redisClient(ClientResources clientResources, RedisConfig redisConfig) {
        return RedisClient.create(clientResources, RedisURI.builder().withHost(redisConfig.getHost())
                .withPort(Integer.parseInt(redisConfig.getPort())).withPassword(redisConfig.getPassword()).build());
    }

    @Bean(destroyMethod = "close")
    StatefulRedisConnection<String, String> connection(RedisClient redisClient) {
        return redisClient.connect();
    }
}
