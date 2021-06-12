package com.infinibet.payload;

import com.infinibet.valid.PasswordMatches;
import lombok.Data;

@Data
@PasswordMatches
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirmation;
}
