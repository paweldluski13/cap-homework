package com.example.exception;

public class WrongStatusException extends RuntimeException {
    public WrongStatusException(String message) {
        super(message);
    }
}
