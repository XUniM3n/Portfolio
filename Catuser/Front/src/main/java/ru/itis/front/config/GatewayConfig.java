package ru.itis.front.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayConfig {
    @Value("${gateway.address}")
    private String gateway;
    @Value("${gateway.prefix}")
    private String gatewayPrefix;
    @Value("${gateway.routes.user.path}")
    private String userServiceRoute;
    @Value("${gateway.routes.auth.path}")
    private String authServiceRoute;
}
