package com.bci.productcrud.exception;

public class DuplicateBarcodeException extends RuntimeException {

    public DuplicateBarcodeException(String message) {
        super(message);
    }
}
