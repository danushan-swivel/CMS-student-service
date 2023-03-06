package com.cms.student.domain.response;

import com.cms.student.domain.entity.Student;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StudentListResponseDto extends ResponseDto{
    private final List<StudentResponseDto> students;

    public  StudentListResponseDto(Page<Student> studentsPage) {
        this.students = convertToResponseDto(studentsPage);
    }

    private List<StudentResponseDto> convertToResponseDto(Page<Student> studentsPage) {
        return studentsPage.stream().map(StudentResponseDto::new).collect(Collectors.toList());
    }
}
