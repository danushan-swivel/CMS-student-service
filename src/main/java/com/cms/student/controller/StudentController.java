package com.cms.student.controller;

import com.cms.student.domain.entity.Student;
import com.cms.student.domain.request.StudentRequestDto;
import com.cms.student.domain.request.UpdateStudentRequestDto;
import com.cms.student.domain.response.StudentListResponseDto;
import com.cms.student.domain.response.StudentResponseDto;
import com.cms.student.enums.ErrorResponseStatus;
import com.cms.student.enums.SuccessResponseStatus;
import com.cms.student.exception.*;
import com.cms.student.service.StudentService;
import com.cms.student.utills.Constants;
import com.cms.student.wrapper.ErrorResponseWrapper;
import com.cms.student.wrapper.ResponseWrapper;
import com.cms.student.wrapper.SuccessResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("api/v1/student")
@RestController
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseWrapper> createStudent(@RequestBody StudentRequestDto studentRequestDto,
                                                         HttpServletRequest request) {
        try {
            if (!studentRequestDto.isRequiredAvailable()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.MISSING_REQUIRED_FIELDS, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (!studentRequestDto.validAge()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_AGE, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            String authToken = request.getHeader(Constants.TOKEN_HEADER);
            Student student = studentService.createStudent(studentRequestDto, authToken);
            var responseDto = new StudentResponseDto(student);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.STUDENT_CREATED, responseDto);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (StudentAlreadyExistsException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.STUDENT_ALREADY_EXISTS, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (InvalidLocationException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_LOCATION, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (ConnectionException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTER_CONNECTION_FAILED, null);
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
    public ResponseEntity<ResponseWrapper> updateStudent(@RequestBody UpdateStudentRequestDto updateStudentRequestDto,
                                                         HttpServletRequest request) {
        try {
            if (!updateStudentRequestDto.isRequiredAvailable()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.MISSING_REQUIRED_FIELDS, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (!updateStudentRequestDto.validAge()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_AGE, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            String authToken = request.getHeader(Constants.TOKEN_HEADER);
            Student student = studentService.updateStudent(updateStudentRequestDto, authToken);
            var responseDto = new StudentResponseDto(student);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.STUDENT_UPDATES, responseDto);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (InvalidStudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_STUDENT, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentAlreadyExistsException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.STUDENT_ALREADY_EXISTS, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (InvalidLocationException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_LOCATION, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (ConnectionException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTER_CONNECTION_FAILED, null);
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
        } catch (InvalidStudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_STUDENT, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
