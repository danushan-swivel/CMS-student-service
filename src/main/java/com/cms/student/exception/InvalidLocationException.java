package com.cms.student.exception;

public class InvalidLocationException extends StudentException {
    public InvalidLocationException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidLocationException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
