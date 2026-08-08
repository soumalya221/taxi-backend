package com.soumalya.taxi_backend.exception;

public class DriverAlreadyExistsException extends RuntimeException {

    public DriverAlreadyExistsException(String message) {
        super(message);
    }

}
