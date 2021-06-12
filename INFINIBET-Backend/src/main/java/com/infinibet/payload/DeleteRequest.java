package com.infinibet.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteRequest {
    @NotNull
    private Long id;
}
