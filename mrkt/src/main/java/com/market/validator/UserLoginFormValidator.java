package com.market.validator;

import com.market.form.UserLoginForm;
import com.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserLoginFormValidator implements Validator {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.getName().equals(UserLoginForm.class.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserLoginForm form = (UserLoginForm) o;

        ValidationUtils.rejectIfEmpty(errors, "email", "empty.email", "Empty email");
        ValidationUtils.rejectIfEmpty(errors, "password", "empty.password", "Empty password");

        if(!VALID_EMAIL_ADDRESS_REGEX.matcher(form.getEmail()).matches())
            errors.reject("invalid.email", "Invalid email");
    }
}
