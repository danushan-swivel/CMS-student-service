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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
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
                log.debug("The required field values {} are missing for create new student",
                        studentRequestDto.toJson());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (!studentRequestDto.validAge()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_AGE, null);
                log.debug("The invalid age {} is given to create new student", studentRequestDto.getAge());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            String authToken = request.getHeader(Constants.TOKEN_HEADER);
            Student student = studentService.createStudent(studentRequestDto, authToken);
            var responseDto = new StudentResponseDto(student);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.STUDENT_CREATED, responseDto);
            log.debug("The new student is created successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (StudentAlreadyExistsException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.STUDENT_ALREADY_EXISTS, null);
            log.error("The student already exist with the firstname: {} and lastname: {}",
                    studentRequestDto.getFirstName(), studentRequestDto.getLastName());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (InvalidLocationException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_TUITION_CLASS_LOCATION, null);
            log.error("The selected tuition class location id {} is not exists", studentRequestDto.getTuitionClassId());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (ConnectionException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTER_CONNECTION_FAILED, null);
            log.error("The connection between micro services is failed");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            log.error("The create new student is failed for student firstname: {} lastname: {}",
                    studentRequestDto.getFirstName(), studentRequestDto.getLastName());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper> getStudentById(@PathVariable String studentId) {
        try {
            var student = studentService.getStudentById(studentId);
            var responseDto = new StudentResponseDto(student);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.READ_STUDENT, responseDto);
            log.debug("The student is retrieved successfully for student id: {}", studentId);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (InvalidStudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_STUDENT, null);
            log.error("The retrieving the student is failed due to invalid student id {}", studentId);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            log.error("The retrieving the student is failed for student id {}", studentId);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseWrapper> getAllStudents() {
        try {
            var studentsPage = studentService.getStudentsPage();
            var responseDto = new StudentListResponseDto(studentsPage);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.READ_STUDENT_LIST, responseDto);
            log.debug("The retrieving all student details is successful");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            log.error("The retrieving all student details is failed");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<ResponseWrapper> updateStudent(@RequestBody UpdateStudentRequestDto updateStudentRequestDto,
                                                         HttpServletRequest request) {
        try {
            if (!updateStudentRequestDto.isRequiredAvailable()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.MISSING_REQUIRED_FIELDS, null);
                log.debug("The updating student is failed by required field values {} are missing for student {}",
                        updateStudentRequestDto.toJson(), updateStudentRequestDto.getStudentId());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (!updateStudentRequestDto.validAge()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_AGE, null);
                log.debug("The invalid age {} is given to update the student: {}", updateStudentRequestDto.getAge(),
                        updateStudentRequestDto.getStudentId());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            String authToken = request.getHeader(Constants.TOKEN_HEADER);
            Student student = studentService.updateStudent(updateStudentRequestDto, authToken);
            var responseDto = new StudentResponseDto(student);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.STUDENT_UPDATES, responseDto);
            log.debug("The student is updated successfully for student id: {}", updateStudentRequestDto.getStudentId());
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (InvalidStudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_STUDENT, null);
            log.error("The student id: {} is not exists for update", updateStudentRequestDto.getStudentId());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentAlreadyExistsException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.STUDENT_ALREADY_EXISTS, null);
            log.error("The student already exist with the firstname: {} and lastname: {} for student id: {}",
                    updateStudentRequestDto.getFirstName(), updateStudentRequestDto.getLastName(),
                    updateStudentRequestDto.getStudentId());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (InvalidLocationException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_TUITION_CLASS_LOCATION, null);
            log.error("The selected tuition class location id {} is not exists for student id: {}",
                    updateStudentRequestDto.getTuitionClassId(), updateStudentRequestDto.getStudentId());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (ConnectionException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTER_CONNECTION_FAILED, null);
            log.error("The student update is failed due to connection between micro services is failed for student id: {}",
                    updateStudentRequestDto.getStudentId());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            log.error("The update student is failed for student firstname: {} lastname: {} for student id: {}",
                    updateStudentRequestDto.getFirstName(), updateStudentRequestDto.getLastName(),
                    updateStudentRequestDto.getStudentId());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<ResponseWrapper> deleteStudent(@PathVariable String studentId) {
        try {
            studentService.deleteStudent(studentId);
            var successResponse = new SuccessResponseWrapper(SuccessResponseStatus.STUDENT_DELETED, null);
            log.debug("The student is deleted successfully for student id: {}", studentId);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (InvalidStudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_STUDENT, null);
            log.error("The deleting student {} is not exists", studentId);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (StudentException e) {
            var response = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            log.error("The deleting student request is failed for student id: {}", studentId);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
