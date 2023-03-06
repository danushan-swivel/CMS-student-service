package com.cms.student.enums;

import lombok.Getter;

@Getter
public enum SuccessResponseStatus {
    STUDENT_CREATED(2000, "The student created successfully"),
    STUDENT_UPDATES(2001, "The student updated successfully"),
    READ_STUDENT_LIST(2002, "Students details retrieved successfully"),
    STUDENT_DELETED(2003, "Student deleted successfully"),
    READ_STUDENT(2004, "Student retrieved successfully");
    private final String message;
    private final int statusCode;

    SuccessResponseStatus(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
