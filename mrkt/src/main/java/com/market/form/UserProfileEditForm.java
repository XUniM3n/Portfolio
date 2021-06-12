package com.market.form;

import lombok.Data;

@Data
public class UserProfileEditForm {

    private boolean changePassword;

    private String oldPassword;

    private String password1;

    private String password2;

    private String bio;

    private String contacts;
}
