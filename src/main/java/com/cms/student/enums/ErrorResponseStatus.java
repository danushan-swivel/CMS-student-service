package com.cms.student.enums;

import lombok.Getter;

@Getter
public enum ErrorResponseStatus {
    INTERNAL_SERVER_ERROR(5000, "Internal server error"),
    MISSING_REQUIRED_FIELDS(4000, "The required fields are missing"),
    INVALID_AGE(4001, "The given age is invalid"),
    INVALID_LOCATION(4030, "The location Id is invalid"),
    INVALID_STUDENT(4050, "The student Id is invalid"),
    ALREADY_PAID(4051, "The payment already made for specific month");
    private final String message;
    private final int statusCode;

    ErrorResponseStatus(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
