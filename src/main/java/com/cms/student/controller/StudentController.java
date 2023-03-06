package com.cms.student.controller;

import com.cms.student.domain.entity.Student;
import com.cms.student.domain.request.StudentRequestDto;
import com.cms.student.domain.request.UpdateStudentRequestDto;
import com.cms.student.domain.response.StudentListResponseDto;
import com.cms.student.domain.response.StudentResponseDto;
import com.cms.student.enums.ErrorResponseStatus;
import com.cms.student.enums.SuccessResponseStatus;
import com.cms.student.exception.InvalidStudentException;
import com.cms.student.exception.StudentException;
import com.cms.student.exception.InvalidLocationException;
import com.cms.student.service.StudentService;
import com.cms.student.wrapper.ErrorResponseWrapper;
import com.cms.student.wrapper.ResponseWrapper;
import com.cms.student.wrapper.SuccessResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/student")
@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseWrapper> createStudent(@RequestBody StudentRequestDto studentRequestDto) {
        try {
            if (!studentRequestDto.isRequiredAvailable()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.MISSING_REQUIRED_FIELDS, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (!studentRequestDto.validAge()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_AGE, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Student student = studentService.createStudent(studentRequestDto);
            var responseDto = new StudentResponseDto(student);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.STUDENT_CREATED, responseDto);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (InvalidLocationException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_LOCATION, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseWrapper> getAllStudents() {
        try {
            var studentsPage = studentService.getStudentsPage();
            var responseDto = new StudentListResponseDto(studentsPage);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.READ_STUDENT_LIST, responseDto);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<ResponseWrapper> updateStudent(@RequestBody UpdateStudentRequestDto updateStudentRequestDto) {
        try {
            if (!updateStudentRequestDto.isRequiredAvailable()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.MISSING_REQUIRED_FIELDS, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (!updateStudentRequestDto.validAge()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_AGE, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Student student = studentService.updateStudent(updateStudentRequestDto);
            var responseDto = new StudentResponseDto(student);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.STUDENT_UPDATES, responseDto);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (InvalidLocationException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_LOCATION, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper> getStudentById(@PathVariable String studentId) {
        try {
            var student = studentService.getStudentById(studentId);
            var responseDto = new StudentResponseDto(student);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.READ_STUDENT, responseDto);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (InvalidStudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_STUDENT, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper> deleteStudent(@PathVariable String studentId) {
        try {
            studentService.deleteStudent(studentId);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.STUDENT_DELETED, null);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
