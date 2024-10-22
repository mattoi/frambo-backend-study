package com.mattoi.frambo_mock.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}