package com.market.service.implementation;

import com.market.form.UserRegistrationForm;
import com.market.model.User;
import com.market.repository.UserRepository;
import com.market.security.role.Role;
import com.market.security.state.UserState;
import com.market.service.EmailService;
import com.market.service.RegistrationService;
import com.market.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;
    private final MoneyServiceImpl moneyService;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    public RegistrationServiceImpl(UserRepository userRepository, PasswordGenerator passwordGenerator, EmailService emailService, MoneyServiceImpl moneyService) {
        this.userRepository = userRepository;
        this.passwordGenerator = passwordGenerator;
        this.emailService = emailService;
        this.moneyService = moneyService;
    }

    @Transactional
    @Override
    public String register(UserRegistrationForm registrationForm) {
        String emailLink = UUID.randomUUID().toString();

        User newUser = User.builder()
                .username(registrationForm.getUsername())
                .password(encoder.encode(registrationForm.getPassword()))
                .email(registrationForm.getEmail())
                .role(Role.USER)
                .regDate(new Timestamp(System.currentTimeMillis()))
                .contacts(registrationForm.getContacts())
                .build();
        if (registrationForm.isConfirmEmail()) {
            newUser.setState(UserState.NOT_CONFIRMED);
            executorService.submit(() ->
                    emailService.sendMail("<h1><a href=\"http://127.0.0.1:8080/link?id=\">http://127.0.0.1:8080/link?id=" + emailLink + "</h1>",
                            "Click this link to activate your account", newUser.getEmail()));
        } else {
            newUser.setState(UserState.CONFIRMED);
            moneyService.createWallet(newUser);
        }

        userRepository.save(newUser);
        return emailLink;
    }

    @Override
    public void confirm(User user){
        user.setEmailLink(null);
        user.setState(UserState.CONFIRMED);
        userRepository.save(user);
    }
}
