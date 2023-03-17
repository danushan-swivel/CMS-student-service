package com.cms.student.exception;

public class ConnectionException extends StudentException {
    public ConnectionException(String errorMessage) {
        super(errorMessage);
    }

    public ConnectionException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
