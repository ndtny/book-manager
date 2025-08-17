package com.david.bookmanager.exception;

/**
 * 库存相关异常
 */
public class InventoryException extends RuntimeException {

    public InventoryException(String message) {
        super(message);
    }

    public InventoryException(String message, Throwable cause) {
        super(message, cause);
    }
} 