package com.market.controller;

import com.market.model.User;
import com.market.service.AdminService;
import com.market.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AuthenticationService service;
    private final AdminService adminService;

    @Autowired
    public AdminController(AuthenticationService service, AdminService adminService) {
        this.service = service;
        this.adminService = adminService;
    }

    @GetMapping("")
    public String getMainAdminPage(Model model, Authentication authentication) {
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("user", service.getUserByAuthentication(authentication));
        return "admin_password_page";
    }

    @PostMapping("")
    public String getNewPasswordOfUserPage(Model model, Authentication authentication,
                                           @RequestParam(value = "id") Long id,
                                           @RequestParam(value = "action") String action) {
        switch (action) {
            case "generate":
                adminService.createTempPassword(id);
                break;
            case "clear":
                adminService.clearTempPassword(id);
                break;
            default:
                break;
        }
        model.addAttribute("users", adminService.getAllUsers().stream()
                .sorted(Comparator.comparing(User::getId)).collect(Collectors.toList()));
        model.addAttribute("user", service.getUserByAuthentication(authentication));
        return "admin_password_page";
    }
}