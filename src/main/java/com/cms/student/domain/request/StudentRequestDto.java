package com.cms.student.domain.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentRequestDto extends RequestDto{
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private int age;
    private int phoneNumber;
    private int grade;
    private String tuitionClassId;

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(firstName) && isNonEmpty(lastName) && isNonEmpty(address) && isNonEmpty(gender)
                && isNonEmpty(String.valueOf(age)) && isNonEmpty(String.valueOf(grade))
                && isNonEmpty(String.valueOf(phoneNumber)) && isNonEmpty(tuitionClassId);
    }

    public boolean validAge() {
        return age > 0;
    }
}
