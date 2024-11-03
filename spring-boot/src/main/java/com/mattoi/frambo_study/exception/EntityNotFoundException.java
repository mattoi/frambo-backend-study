package com.mattoi.frambo_study.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}