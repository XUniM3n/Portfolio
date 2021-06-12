package com.infinibet.payload;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class SuccessResponse extends ApiResponse {
    public SuccessResponse(String message) {
        super(true, message);
    }
}
