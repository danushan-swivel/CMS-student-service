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
    private String studentId;
    private String firstName;
    private String lastName;
    private String address;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int age;
    private int phoneNumber;
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;
    private String locationId;
    private long joinedDate;
    private long updatedAt;
    private boolean isDeleted;

    public Student (StudentRequestDto studentRequestDto) {
        this.studentId = PREFIX + UUID.randomUUID();
        this.firstName = studentRequestDto.getFirstName();
        this.lastName = studentRequestDto.getLastName();
        this.address = studentRequestDto.getAddress();
        this.gender = Gender.valueOf(studentRequestDto.getGender().toUpperCase());
        this.age = studentRequestDto.getAge();
        this.phoneNumber = studentRequestDto.getPhoneNumber();
        this.studentStatus = StudentStatus.COMING;
        this.locationId = studentRequestDto.getLocationId();
        this.joinedDate = this.updatedAt = System.currentTimeMillis();
        this.isDeleted = false;
    }

    public void update(UpdateStudentRequestDto updateStudentRequestDto) {
        this.firstName = updateStudentRequestDto.getFirstName();
        this.lastName = updateStudentRequestDto.getLastName();
        this.address = updateStudentRequestDto.getAddress();
        this.gender = Gender.valueOf(updateStudentRequestDto.getGender().toUpperCase());
        this.age = updateStudentRequestDto.getAge();
        this.phoneNumber = updateStudentRequestDto.getPhoneNumber();
        this.locationId = updateStudentRequestDto.getLocationId();
        this.updatedAt = System.currentTimeMillis();
    }
}
