package com.cms.student.domain.response;

import com.cms.student.domain.entity.Student;
import com.cms.student.enums.Gender;
import com.cms.student.enums.StudentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponseDto extends ResponseDto{
    private String studentId;
    private String firstName;
    private String lastName;
    private String address;
    private Gender gender;
    private int age;
    private int grade;
    private int phoneNumber;
    private StudentStatus studentStatus;
    private String locationId;
    private long joinedDate;
    private long updatedAt;
    private boolean isDeleted;

    public StudentResponseDto(Student student) {
        this.studentId = student.getStudentId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.address = student.getAddress();
        this.gender = student.getGender();
        this.age = student.getAge();
        this.grade = student.getGrade();
        this.phoneNumber = student.getPhoneNumber();
        this.studentStatus = student.getStudentStatus();
        this.locationId = student.getLocationId();
        this.joinedDate = student.getUpdatedAt();
        this.updatedAt = student.getUpdatedAt();
        this.isDeleted = student.isDeleted();
    }
}
