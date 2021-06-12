package com.market.service;

import com.market.form.UserProfileEditForm;
import com.market.model.User;

public interface ProfileService {
    void changeProfileInfo(UserProfileEditForm form, User user);
}
