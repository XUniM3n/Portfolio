package ru.itis.gateway.app;

import com.google.gson.Gson;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.ClientResources;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.itis.gateway.config.RedisConfig;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 13.04.2018
 * ZuulApplication
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
@EnableSwagger2
@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@ComponentScan("ru.itis.gateway")
//TODO @EnableOAuth2Sso
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class);
    }

    @Bean(destroyMethod = "shutdown")
    public RedisClient redisClient(ClientResources clientResources, RedisConfig redisConfig) {
        return RedisClient.create(clientResources, RedisURI.builder().withHost(redisConfig.getHost())
                .withPort(Integer.parseInt(redisConfig.getPort())).withPassword(redisConfig.getPassword()).build());
    }

    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, String> connection(RedisClient redisClient) {
        return redisClient.connect();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
