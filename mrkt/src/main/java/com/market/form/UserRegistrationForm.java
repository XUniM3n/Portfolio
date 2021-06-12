package com.market.form;

import lombok.Data;

@Data
public class UserRegistrationForm {

    private String email;

    private String username;

    private String password;

    private String password2;

    private String contacts;

    private boolean confirmEmail;
}
