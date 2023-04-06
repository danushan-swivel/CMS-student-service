package com.cms.student.enums;

import lombok.Getter;

@Getter
public enum ErrorResponseStatus {
    INTERNAL_SERVER_ERROR("Internal server error"),
    INTER_CONNECTION_FAILED("Internal server connection error"),
    MISSING_REQUIRED_FIELDS("The required fields are missing"),
    INVALID_AGE("The given age is invalid"),
    INVALID_TUITION_CLASS_LOCATION("The location Id is invalid"),
    INVALID_STUDENT("The student Id is invalid"),
    STUDENT_ALREADY_EXISTS("The student already exists");
    private final String message;

    ErrorResponseStatus(String message) {
        this.message = message;
    }
}
