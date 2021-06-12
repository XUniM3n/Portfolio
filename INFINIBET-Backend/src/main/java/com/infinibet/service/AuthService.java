package com.infinibet.service;

import com.infinibet.model.Person;

public interface AuthService {
    boolean signup(String username, String password);

    boolean changePassword(Person person, String oldPassword, String newPassword);
}
