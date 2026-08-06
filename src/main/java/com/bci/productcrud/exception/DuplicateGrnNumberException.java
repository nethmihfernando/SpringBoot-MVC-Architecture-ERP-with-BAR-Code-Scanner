package com.bci.productcrud.exception;

public class DuplicateGrnNumberException extends RuntimeException {

    public DuplicateGrnNumberException(String message) {
        super(message);
    }
}
