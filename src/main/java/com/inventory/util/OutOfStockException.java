package com.inventory.util;

public class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}
