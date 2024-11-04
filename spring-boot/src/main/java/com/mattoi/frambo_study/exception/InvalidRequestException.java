package com.mattoi.frambo_study.exception;

import java.util.List;

public class InvalidRequestException extends RuntimeException {
    private final List<String> errorMessages;

    public InvalidRequestException(List<String> errorMessages, Throwable err) {
        super("Invalid request fields");
        this.errorMessages = errorMessages;
    }

    public List<String> getMessages() {
        return errorMessages;
    }
}