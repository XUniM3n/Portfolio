package com.infinibet.service.impl;

import com.infinibet.exception.AppException;
import com.infinibet.exception.BadRequestException;
import com.infinibet.model.Person;
import com.infinibet.model.Role;
import com.infinibet.model.RoleName;
import com.infinibet.repository.PersonRepo;
import com.infinibet.repository.RoleRepo;
import com.infinibet.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final PersonRepo personRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(PersonRepo personRepo, PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.personRepo = personRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    @Override
    public boolean signup(String username, String password) {
        if (personRepo.existsByUsername(username)) {
            logger.error("Error while signup: Username {} already taken", username);
            throw new BadRequestException("Username already taken");
        }

        Person person = new Person(username, password);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Role userRole = roleRepo.findByName(RoleName.ROLE_PLAYER)
                .orElseThrow(() -> new AppException("User Role not set"));
        person.setRoles(Collections.singleton(userRole));
        personRepo.save(person);

        return true;
    }

    @Override
    public boolean changePassword(Person person, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, person.getPassword())) {
            logger.error("Error while changing password: Person {}", person.getUsername());
            throw new BadRequestException("Incorrect old password");
        }

        person.setPassword(passwordEncoder.encode(newPassword));
        personRepo.save(person);

        return true;
    }
}
