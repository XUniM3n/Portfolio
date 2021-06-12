package com.infinibet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    private final boolean success = false;

    public UnauthorizedException() {
        super("You are unauthorized to interact with these resource");
    }
}
