package ru.itis.front.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.itis.front.form.UserAuthForm;

@Component
public class UserRegistrationFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {

        return aClass.getName().equals(UserAuthForm.class.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserAuthForm form = (UserAuthForm) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty.password");
    }
}
