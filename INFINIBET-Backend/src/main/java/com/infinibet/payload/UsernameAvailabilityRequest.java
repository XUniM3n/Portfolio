package com.infinibet.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsernameAvailabilityRequest {
    @NotNull
    private String username;
}
