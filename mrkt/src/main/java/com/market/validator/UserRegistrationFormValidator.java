package com.market.validator;

import com.market.form.UserRegistrationForm;
import com.market.model.User;
import com.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class UserRegistrationFormValidator implements Validator {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.getName().equals(UserRegistrationForm.class.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegistrationForm form = (UserRegistrationForm) o;

        Optional<User> existedUser = userRepository.findOneByUsername(form.getUsername());

        if (existedUser.isPresent())
            errors.reject("bad.login", "Login is already in use");

        if (form.getPassword().length() < 4)
            errors.reject("short.password", "Password should contain at least 8 characters.");

        ValidationUtils.rejectIfEmpty(errors, "name", "empty.name", "Empty login");
        ValidationUtils.rejectIfEmpty(errors, "email", "empty.email",
                "Email field should not be empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "empty.password",
                "Password field should not be empty");
        ValidationUtils.rejectIfEmpty(errors, "password2", "empty.password",
                "Password confirmation field should not be empty");

        if (!form.getPassword().equals(form.getPassword2()))
            errors.reject("bad.password.match", "Passwords do not match");

        if (!EMAIL_REGEX.matcher(form.getEmail()).matches())
            errors.reject("invalid.email", "Invalid email");
    }
}
