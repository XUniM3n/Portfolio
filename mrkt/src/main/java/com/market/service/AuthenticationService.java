package com.market.service;

import com.market.model.User;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    User getUserByAuthentication(Authentication authentication);
}
