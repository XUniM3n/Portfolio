package com.infinibet.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class UsernameAvailabilityResponse {
    @NotNull
    private Boolean available;
}
