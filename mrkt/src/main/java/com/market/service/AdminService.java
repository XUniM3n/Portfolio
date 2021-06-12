package com.market.service;

import com.market.model.User;

import java.util.List;

public interface AdminService {

    List<User> getAllUsers();

    void createTempPassword(Long userId);

    void clearTempPassword(Long userId);
}
