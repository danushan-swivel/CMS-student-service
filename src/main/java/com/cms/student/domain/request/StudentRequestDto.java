package com.cms.student.domain.request;

import lombok.Getter;

@Getter
public class StudentRequestDto extends RequestDto{
    private String firstName;
    private String lastName;
    private String address;
    private String gender; //Drop d
    private int age; //calender
    private int phoneNumber;
    private String locationId; // dropdowm

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(firstName) && isNonEmpty(lastName) && isNonEmpty(address) && isNonEmpty(gender)
                && isNonEmpty(String.valueOf(age)) && isNonEmpty(String.valueOf(phoneNumber)) && isNonEmpty(locationId);
    }

    public boolean validAge() {
        return age > 0;
    }
}
