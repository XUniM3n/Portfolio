package ru.itis.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.itis.front.form.UserAuthForm;
import ru.itis.front.service.RegistrationService;
import ru.itis.front.validation.UserRegistrationFormValidator;

@Controller
public class WebSocketController {

    private final UserRegistrationFormValidator userRegistrationFormValidator;
    private final RegistrationService registrationService;

    @Autowired
    public WebSocketController(RegistrationService registrationService, UserRegistrationFormValidator userRegistrationFormValidator) {
        this.registrationService = registrationService;
        this.userRegistrationFormValidator = userRegistrationFormValidator;
    }


    @MessageMapping("/signup.signup")
    public UserAuthForm addUser(@Payload @ModelAttribute("regForm") UserAuthForm form, SimpMessageHeaderAccessor sha) {
        // Add username in web socket session

        registrationService.register(form, sha.getUser().getName());
        return form;
    }

    @InitBinder("regForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(userRegistrationFormValidator);
    }

}
