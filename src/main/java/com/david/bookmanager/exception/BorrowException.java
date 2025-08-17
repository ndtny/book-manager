package com.david.bookmanager.exception;

/**
 * 借阅相关异常
 */
public class BorrowException extends RuntimeException {

    public BorrowException(String message) {
        super(message);
    }

    public BorrowException(String message, Throwable cause) {
        super(message, cause);
    }
} 