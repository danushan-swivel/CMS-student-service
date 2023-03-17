package com.cms.student.exception;

public class StudentException extends RuntimeException{
    public StudentException(String errorMessage) {
        super(errorMessage);
    }

    public StudentException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
