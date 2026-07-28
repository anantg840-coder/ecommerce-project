package com.ecommerce.project.Exception;

public class ApiException extends RuntimeException{
    public static final long serialVersionUID = 1L;
    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }
}
