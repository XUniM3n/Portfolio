package com.market.controller;

import com.market.form.UserRegistrationForm;
import com.market.model.User;
import com.market.repository.UserRepository;
import com.market.security.state.UserState;
import com.market.service.RegistrationService;
import com.market.validator.UserRegistrationFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class RegistrationController {
    private final UserRepository userRepository;
    private final RegistrationService userRegService;
    private final UserRegistrationFormValidator userRegistrationFormValidator;

    @Autowired
    public RegistrationController(UserRepository userRepository, RegistrationService userRegService, UserRegistrationFormValidator userRegistrationFormValidator) {
        this.userRepository = userRepository;
        this.userRegService = userRegService;
        this.userRegistrationFormValidator = userRegistrationFormValidator;
    }

    @PostMapping(value = "/register")
    public String signUp(@Valid @ModelAttribute("regForm") UserRegistrationForm userRegistrationForm,
                         BindingResult errors, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            attributes.addFlashAttribute("error", errors.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/register";
        }
        String emailLink = userRegService.register(userRegistrationForm);
        if (userRegistrationForm.isConfirmEmail())
            attributes.addAttribute("success", "We've sent you email. " +
                    "Please follow the instruction to activate your account");
        else
            attributes.addAttribute("success", "Successfully registered");
        return "redirect:/";
    }

    @GetMapping(value = "/register")
    public String signUp(Authentication authentication) {
        if (authentication != null) {
            return "redirect:/";
        }
        return "register_page";
    }

    @GetMapping(value = "/link", params = {"id"})
    public String confirmEmail(@RequestParam(value = "id") String linkId, RedirectAttributes attributes) {
        Optional<User> userByLink = userRepository.findOneByEmailLink(linkId);
        if (userByLink.isPresent()) {
            User user = userByLink.get();
            if (user.getState() == UserState.CONFIRMED) {
                attributes.addFlashAttribute("error", "Wrong Link");
            } else {
                attributes.addAttribute("success", "Successfully confirmed account");
                userRegService.confirm(user);
            }
        }
        return "redirect:/";
    }

    @InitBinder("regForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(userRegistrationFormValidator);
    }
}
