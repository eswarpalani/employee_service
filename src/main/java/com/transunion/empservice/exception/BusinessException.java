package com.transunion.empservice.exception;

public class BusinessException extends RuntimeException {
    private String message;
    private int statusCode;

    public BusinessException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
