package com.example.empleados.application.exception;

public class InvalidClaveException extends RuntimeException {

    public InvalidClaveException(String message) {
        super(message);
    }
}
