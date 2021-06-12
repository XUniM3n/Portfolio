package com.market.service.implementation;

import com.market.form.UserProfileEditForm;
import com.market.model.User;
import com.market.repository.UserRepository;
import com.market.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfileServiceImpl implements ProfileService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void changeProfileInfo(UserProfileEditForm form, User user) {
        user.setPassword(form.getPassword1());
        user.setContacts(form.getContacts());
        userRepository.saveAndFlush(user);
    }
}
