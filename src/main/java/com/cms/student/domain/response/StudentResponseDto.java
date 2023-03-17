package com.cms.student.domain.response;

import com.cms.student.domain.entity.Student;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class StudentResponseDto extends ResponseDto {
    private String studentId;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private int age;
    private int grade;
    private int phoneNumber;
    private String studentStatus;
    private String tuitionClassId;
    private Date joinedDate;
    private Date updatedAt;
    private boolean isDeleted;

    public StudentResponseDto(Student student) {
        this.studentId = student.getStudentId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.address = student.getAddress();
        this.gender = student.getGender().getGenderValue();
        this.age = student.getAge();
        this.grade = student.getGrade();
        this.phoneNumber = student.getPhoneNumber();
        this.studentStatus = student.getStudentStatus().getStatus();
        this.tuitionClassId = student.getTuitionClassId();
        this.joinedDate = student.getUpdatedAt();
        this.updatedAt = student.getUpdatedAt();
        this.isDeleted = student.isDeleted();
    }
}
