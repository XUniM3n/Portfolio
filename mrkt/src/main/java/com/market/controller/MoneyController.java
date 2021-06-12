package com.market.controller;

import com.market.form.SendMoneyForm;
import com.market.model.Transaction;
import com.market.model.User;
import com.market.model.Wallet;
import com.market.repository.TransactionRepository;
import com.market.repository.UserRepository;
import com.market.repository.WalletRepository;
import com.market.security.role.Role;
import com.market.service.AuthenticationService;
import com.market.service.MoneyService;
import com.market.validator.SendMoneyFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MoneyController {
    private final AuthenticationService authenticationService;
    private final MoneyService moneyService;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    private final SendMoneyFormValidator sendMoneyFormValidator;

    @Autowired
    public MoneyController(AuthenticationService authenticationService, MoneyService moneyService,
                           UserRepository userRepository, WalletRepository walletRepository,
                           TransactionRepository transactionRepository, SendMoneyFormValidator sendMoneyFormValidator) {
        this.authenticationService = authenticationService;
        this.moneyService = moneyService;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.sendMoneyFormValidator = sendMoneyFormValidator;
    }

    @GetMapping("/money")
    public String dashboard(Authentication authentication, Model model) {
        User user = authenticationService.getUserByAuthentication(authentication);
        model.addAttribute("user", authenticationService.getUserByAuthentication(authentication));
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(user.getId())).collect(Collectors.toList());
        Collections.reverse(transactions);
        model.addAttribute("transactions", transactions);
        return "money_page";
    }

    @GetMapping("/admin/money")
    public String adminMoney(Authentication authentication, Model model) {
        List<Wallet> wallets = walletRepository.findAll();
        model.addAttribute("users", userRepository.findAll().stream()
                .filter(u -> u.getRole().equals(Role.USER))
                .collect(Collectors.toList()));
        model.addAttribute("user", authenticationService.getUserByAuthentication(authentication));
        return "admin-money";
    }

    @PostMapping("/money/create")
    public String createWallet(Authentication authentication, Model model, @RequestParam(value = "id", required = false) Long id) {
        User loggedUser = authenticationService.getUserByAuthentication(authentication);
        User user;
        if (id == null) {
            user = loggedUser;
        } else {
            user = userRepository.findById(id).get();
        }
        if (user.getRole().equals(Role.USER) && (loggedUser.getId().equals(user.getId())) || !loggedUser.getRole().equals(Role.USER)) {
            moneyService.createWallet(user);
        } else {
            // TODO Better error handling.
            return "404";
        }
        model.addAttribute("users", userRepository.findAll().stream()
                .filter(u -> u.getRole().equals(Role.USER))
                .collect(Collectors.toList()));
        model.addAttribute("user", loggedUser);
        return "admin-money";
    }

    @PostMapping("/money/send")
    public String sendMoney(Authentication authentication, Model model, @ModelAttribute("sendMoneyForm") @Valid SendMoneyForm form) {
        User user = authenticationService.getUserByAuthentication(authentication);
        moneyService.sendMoney(user, form.getAddress(), form.getAmount());

        model.addAttribute("users", userRepository.findAll().stream()
                .filter(u -> u.getRole().equals(Role.USER))
                .collect(Collectors.toList()));
        model.addAttribute("user", user);
        return "redirect:/money";
    }

    @InitBinder("sendMoneyForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(sendMoneyFormValidator);
    }
}
