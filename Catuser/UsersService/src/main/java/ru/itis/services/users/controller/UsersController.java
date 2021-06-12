package ru.itis.services.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.services.users.model.User;
import ru.itis.services.users.repository.UserRepository;

import java.util.List;

@RestController
public class UsersController {
    private final UserRepository userRepository;

    @Autowired
    UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/users", produces = "application/json")
    public ResponseEntity<List<User>> getInfoAbout() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
