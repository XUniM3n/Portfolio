package com.infinibet.payload;

import com.infinibet.valid.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
@NoArgsConstructor
@PasswordMatches
public class SignUpRequest {
    @NotNull
    @Size(min = 3, max = 15)
    private String username;

    @NotNull
    @Size(min = 6, max = 64)
    private String password;

    @NotNull
    @Size(min = 6, max = 64)
    private String passwordConfirmation;
}
