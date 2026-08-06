package com.bci.productcrud.exception;

public class InvalidPurchaseOrderStateException extends RuntimeException {

    public InvalidPurchaseOrderStateException(String message) {
        super(message);
    }
}
