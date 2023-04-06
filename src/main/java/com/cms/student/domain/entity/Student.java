package com.cms.student.domain.entity;

import com.cms.student.domain.request.StudentRequestDto;
import com.cms.student.domain.request.UpdateStudentRequestDto;
import com.cms.student.enums.Gender;
import com.cms.student.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
@Entity
public class Student {
    private static final String PREFIX = "sid-";
    @Id
    @Column(length = 50)
    private String studentId;
    @Column(length = 50)
    private String firstName;
    @Column(length = 50)
    private String lastName;
    @Column(length = 100)
    private String address;
    @Column(length = 6)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int age;
    private int phoneNumber;
    private int grade;
    @Column(length = 6)
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;
    @Column(length = 50)
    private String tuitionClassId;
    private Date joinedDate;
    private Date updatedAt;
    private boolean isDeleted;

    public Student (StudentRequestDto studentRequestDto) {
        this.studentId = PREFIX + UUID.randomUUID();
        this.firstName = studentRequestDto.getFirstName();
        this.lastName = studentRequestDto.getLastName();
        this.address = studentRequestDto.getAddress();
        this.gender = Gender.valueOf(studentRequestDto.getGender().toUpperCase());
        this.age = studentRequestDto.getAge();
        this.grade = studentRequestDto.getGrade();
        this.phoneNumber = studentRequestDto.getPhoneNumber();
        this.studentStatus = StudentStatus.valueOf(StudentStatus.COMING.name());
        this.tuitionClassId = studentRequestDto.getTuitionClassId();
        this.joinedDate = this.updatedAt = new Date(System.currentTimeMillis());
        this.isDeleted = false;
    }

    public void update(UpdateStudentRequestDto updateStudentRequestDto) {
        this.firstName = updateStudentRequestDto.getFirstName();
        this.lastName = updateStudentRequestDto.getLastName();
        this.address = updateStudentRequestDto.getAddress();
        this.gender = Gender.valueOf(updateStudentRequestDto.getGender().toUpperCase());
        this.age = updateStudentRequestDto.getAge();
        this.phoneNumber = updateStudentRequestDto.getPhoneNumber();
        this.tuitionClassId = updateStudentRequestDto.getTuitionClassId();
        this.updatedAt = new Date(System.currentTimeMillis());
    }
}
