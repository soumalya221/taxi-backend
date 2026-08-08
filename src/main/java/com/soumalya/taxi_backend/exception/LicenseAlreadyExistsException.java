package com.soumalya.taxi_backend.exception;

public class LicenseAlreadyExistsException extends RuntimeException {

    public LicenseAlreadyExistsException(String message) {
        super(message);
    }

}