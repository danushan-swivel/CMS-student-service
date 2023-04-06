package com.cms.student.controller;

import com.cms.student.domain.entity.Student;
import com.cms.student.domain.request.StudentRequestDto;
import com.cms.student.domain.request.UpdateStudentRequestDto;
import com.cms.student.domain.response.StudentListResponseDto;
import com.cms.student.domain.response.StudentResponseDto;
import com.cms.student.enums.ErrorResponseStatus;
import com.cms.student.enums.SuccessResponseStatus;
import com.cms.student.service.StudentService;
import com.cms.student.utills.Constants;
import com.cms.student.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping("api/v1/student")
@RestController
public class StudentController extends BaseController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseWrapper> createStudent(@RequestBody StudentRequestDto studentRequestDto,
                                                         HttpServletRequest request) {

        if (!studentRequestDto.isRequiredAvailable()) {
            log.debug("The required field values {} are missing for create new student",
                    studentRequestDto.toJson());
            return getErrorResponse(ErrorResponseStatus.MISSING_REQUIRED_FIELDS);
        }
        if (!studentRequestDto.validAge()) {
            log.debug("The invalid age {} is given to create new student", studentRequestDto.getAge());
            return getErrorResponse(ErrorResponseStatus.INVALID_AGE);
        }
        String authToken = request.getHeader(Constants.TOKEN_HEADER);
        Student student = studentService.createStudent(studentRequestDto, authToken);
        var responseDto = new StudentResponseDto(student);
        log.debug("The new student is created successfully");
        return getSuccessResponse(SuccessResponseStatus.STUDENT_CREATED, responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper> getStudentById(@PathVariable String studentId) {

        var student = studentService.getStudentById(studentId);
        var responseDto = new StudentResponseDto(student);
        log.debug("The student is retrieved successfully for student id: {}", studentId);
        return getSuccessResponse(SuccessResponseStatus.READ_STUDENT, responseDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<ResponseWrapper> getAllStudents() {

        var studentsPage = studentService.getStudentsPage();
        var responseDto = new StudentListResponseDto(studentsPage);
        log.debug("The retrieving all student details is successful");
        return getSuccessResponse(SuccessResponseStatus.READ_STUDENT_LIST, responseDto, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<ResponseWrapper> updateStudent(@RequestBody UpdateStudentRequestDto updateStudentRequestDto,
                                                         HttpServletRequest request) {
        if (!updateStudentRequestDto.isRequiredAvailable()) {
            log.debug("The updating student is failed by required field values {} are missing for student {}",
                    updateStudentRequestDto.toJson(), updateStudentRequestDto.getStudentId());
            return getErrorResponse(ErrorResponseStatus.MISSING_REQUIRED_FIELDS);
        }
        if (!updateStudentRequestDto.validAge()) {
            log.debug("The invalid age {} is given to update the student: {}", updateStudentRequestDto.getAge(),
                    updateStudentRequestDto.getStudentId());
            return getErrorResponse(ErrorResponseStatus.INVALID_AGE);
        }
        String authToken = request.getHeader(Constants.TOKEN_HEADER);
        Student student = studentService.updateStudent(updateStudentRequestDto, authToken);
        var responseDto = new StudentResponseDto(student);
        log.debug("The student is updated successfully for student id: {}", updateStudentRequestDto.getStudentId());
        return getSuccessResponse(SuccessResponseStatus.STUDENT_UPDATES, responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper> deleteStudent(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        log.debug("The student is deleted successfully for student id: {}", studentId);
        return getSuccessResponse(SuccessResponseStatus.STUDENT_DELETED, null, HttpStatus.OK);
    }
}
