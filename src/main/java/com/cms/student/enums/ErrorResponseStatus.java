package com.cms.student.enums;

import lombok.Getter;

@Getter
public enum ErrorResponseStatus {
    INTERNAL_SERVER_ERROR(5000, "Internal server error"),
    INTER_CONNECTION_FAILED(5001, "Internal server connection error"),
    MISSING_REQUIRED_FIELDS(4000, "The required fields are missing"),
    INVALID_AGE(4001, "The given age is invalid"),
    INVALID_TUITION_CLASS_LOCATION(4030, "The location Id is invalid"),
    INVALID_STUDENT(4050, "The student Id is invalid"),
    ALREADY_PAID(4051, "The payment already made for specific month"),
    STUDENT_ALREADY_EXISTS(4002, "The student already exists");
    private final String message;
    private final int statusCode;

    ErrorResponseStatus(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
