package com.infinibet.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SetEventResultRequest {
    @NotNull
    private Long eventId;
    @NotNull
    private Long outcomeId;
}
