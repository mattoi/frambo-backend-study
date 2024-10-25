package com.mattoi.frambo_study.exception;

public class CustomerNotFoundException extends EntityNotFoundException {
    public CustomerNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
