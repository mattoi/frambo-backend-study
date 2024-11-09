package com.mattoi.frambo_study.exception;

import java.util.List;

public class ProductsNotFoundException extends EntityNotFoundException {
    private final List<String> errorMessages;

    public ProductsNotFoundException(String errorMessage, List<String> errorMessages, Throwable err) {
        super(errorMessage, err);
        this.errorMessages = errorMessages;
    }

    public List<String> getMessages() {
        return errorMessages;
    }
}