package com.cms.student.domain.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStudentRequestDto extends RequestDto{
    private String studentId;
    private String firstName;
    private String lastName;
    private String address;
    private String studentStatus;
    private String gender;
    private int grade;
    private int age;
    private int phoneNumber;
    private String tuitionClassId;

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(firstName) && isNonEmpty(lastName) && isNonEmpty(address) && isNonEmpty(gender)
                && isNonEmpty(String.valueOf(age)) && isNonEmpty(String.valueOf(grade)) && isNonEmpty(String.valueOf(phoneNumber)) && isNonEmpty(studentId)
                && isNonEmpty(tuitionClassId) && isNonEmpty(studentStatus);
    }

    public boolean validAge() {
        return age > 0;
    }
}
