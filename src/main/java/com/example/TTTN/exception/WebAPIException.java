package com.example.TTTN.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WebAPIException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public WebAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
