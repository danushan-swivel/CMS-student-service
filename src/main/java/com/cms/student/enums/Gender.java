package com.cms.student.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String genderValue;

    Gender(String genderValue) {
        this.genderValue = genderValue;
    }
}
