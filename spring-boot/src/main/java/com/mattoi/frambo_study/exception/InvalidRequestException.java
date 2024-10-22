package com.mattoi.frambo_study.exception;

import java.util.List;

public class InvalidRequestException extends Exception {
private final List<String> errorMessages;
    public InvalidRequestException(String errorMessage, List<String> errorMessages, Throwable err){
        super(errorMessage);
        this.errorMessages = errorMessages;
    }

    public List<String> getMessages(){
        return errorMessages;
    }
}