package com.market.controller;

import com.market.repository.UserRepository;
import com.market.validator.UserLoginFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class AuthController {
    private final UserRepository userRepository;
    private final UserLoginFormValidator userLoginFormValidator;

    @Autowired
    public AuthController(UserRepository userRepository, UserLoginFormValidator userLoginFormValidator) {
        this.userRepository = userRepository;
        this.userLoginFormValidator = userLoginFormValidator;
    }

    @GetMapping("/login")
    public String login(Model model, Authentication authentication) {
        if (authentication != null) {
            return "redirect:/";
        }
        return "login_page";
    }

    @InitBinder("loginForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(userLoginFormValidator);
    }
}
