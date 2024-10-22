package com.mattoi.frambo_mock.exception;

import java.util.List;

public class InvalidRequestException extends Exception {
private final List<String> errors;
    public InvalidRequestException(String errorMessage, List<String> errors, Throwable err){
        super(errorMessage);
        this.errors = errors;
    }

    public List<String> getErrors(){
        return errors;
    }
}