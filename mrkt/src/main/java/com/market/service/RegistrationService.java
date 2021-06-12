package com.market.service;

import com.market.form.UserRegistrationForm;
import com.market.model.User;

public interface RegistrationService {
    String register(UserRegistrationForm signupForm);

    void confirm(User user);
}
