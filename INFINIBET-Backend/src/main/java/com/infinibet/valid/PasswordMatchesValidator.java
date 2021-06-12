package com.infinibet.valid;

import com.infinibet.payload.ChangePasswordRequest;
import com.infinibet.payload.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof SignUpRequest) {
            SignUpRequest request = (SignUpRequest) obj;
            return request.getPassword().equals(request.getPasswordConfirmation());
        } else if (obj instanceof ChangePasswordRequest) {
            ChangePasswordRequest request = (ChangePasswordRequest) obj;
            return request.getNewPassword().equals(request.getNewPasswordConfirmation());
        } else {
            return false;
        }
    }
}
