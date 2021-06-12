package com.market.validator;

import com.market.form.UserProfileEditForm;
import com.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserProfileEditFormValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.getName().equals(UserProfileEditForm.class.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserProfileEditForm form = (UserProfileEditForm) o;

        ValidationUtils.rejectIfEmpty(errors, "name", "empty.name", "Empty name");
        if (form.isChangePassword()) {
            ValidationUtils.rejectIfEmpty(errors, "password1", "empty.password",
                    "Password field should not be empty");
            ValidationUtils.rejectIfEmpty(errors, "password2", "empty.password1",
                    "Password field should not be empty");
            if (!form.getPassword1().equals(form.getPassword2()))
                errors.reject("password.dontmatch");
        }

        if (form.getContacts().length() > 100) {
            errors.reject("invalid.contacts", "contacts length should be less than 100");
        }
    }
}
