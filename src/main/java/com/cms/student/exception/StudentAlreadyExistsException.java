package com.cms.student.exception;

public class StudentAlreadyExistsException extends StudentException {
    public StudentAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }

    public StudentAlreadyExistsException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
