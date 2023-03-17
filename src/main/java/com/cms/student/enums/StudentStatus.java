package com.cms.student.enums;

import lombok.Getter;

@Getter
public enum StudentStatus {
    COMING("Coming"),
    STOPPED("Stopped"),
    SUSPENDED("Suspended");

    private final String status;

    StudentStatus(String status) {
        this.status = status;
    }
}
