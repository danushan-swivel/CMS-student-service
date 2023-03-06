package com.cms.student.domain.request;

import lombok.Getter;

@Getter
public class UpdateStudentRequestDto extends RequestDto{
    private String studentId;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private int age;
    private int phoneNumber;
    private String locationId;

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(firstName) && isNonEmpty(lastName) && isNonEmpty(address) && isNonEmpty(gender)
                && isNonEmpty(String.valueOf(age)) && isNonEmpty(String.valueOf(phoneNumber)) && isNonEmpty(studentId)
                && isNonEmpty(locationId);
    }

    public boolean validAge() {
        return age > 0;
    }
}
