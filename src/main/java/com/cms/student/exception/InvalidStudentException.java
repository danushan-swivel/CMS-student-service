package com.cms.student.exception;

public class InvalidStudentException extends StudentException {
    public InvalidStudentException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidStudentException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
