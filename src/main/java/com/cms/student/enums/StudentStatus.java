package com.cms.student.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum StudentStatus {
    COMING("Coming"),
    STOPPED("Stopped"),
    SUSPENDED("Suspended");

    private final String studentStatus;

    StudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }
}
