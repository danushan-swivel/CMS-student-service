package com.cms.student.enums;

import lombok.Getter;

@Getter
public enum SuccessResponseStatus {
    STUDENT_CREATED("The student created successfully"),
    STUDENT_UPDATES("The student updated successfully"),
    READ_STUDENT_LIST("Students details retrieved successfully"),
    STUDENT_DELETED("Student deleted successfully"),
    READ_STUDENT("Student retrieved successfully");
    private final String message;

    SuccessResponseStatus(String message) {
        this.message = message;
    }
}
