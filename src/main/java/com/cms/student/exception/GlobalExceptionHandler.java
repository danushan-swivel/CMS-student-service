package com.cms.student.exception;

import com.cms.student.enums.ErrorResponseStatus;
import com.cms.student.wrapper.ErrorResponseWrapper;
import com.cms.student.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * This method handle connection failed exception response
     *
     * @param exception microservices connection failed
     * @return ErrorResponse/BadRequest
     */
    @ExceptionHandler(ConnectionException.class)
    public ResponseEntity<ResponseWrapper> connectionException(ConnectionException exception) {
        var wrapper = new ErrorResponseWrapper(ErrorResponseStatus.INTER_CONNECTION_FAILED, HttpStatus.BAD_REQUEST);
        log.error("The connection failed between micro services. Error message: {}", exception.getMessage());
        return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
    }
    /**
     * This method handle invalid location exception response
     *
     * @param exception invalid location exception
     * @return ErrorResponse/BadRequest
     */
    @ExceptionHandler(InvalidLocationException.class)
    public ResponseEntity<ResponseWrapper> invalidLocationException(InvalidLocationException exception) {
        var wrapper = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_TUITION_CLASS_LOCATION, HttpStatus.BAD_REQUEST);
        log.error("The selected tuition class location id is not exists. Error message: {}", exception.getMessage());
        return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
    }
    /**
     * This method handle student already exists exception response
     *
     * @param exception student already exists exception
     * @return ErrorResponse/BadRequest
     */
    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper> studentAlreadyExistException(StudentAlreadyExistsException exception) {
        var wrapper = new ErrorResponseWrapper(ErrorResponseStatus.STUDENT_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        log.error("The student already exist. Error message: {}", exception.getMessage());
        return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
    }
    /**
     * This method handle failed student exception response
     *
     * @param exception invalid student exception
     * @return ErrorResponse/BadRequest
     */
    @ExceptionHandler(InvalidStudentException.class)
    public ResponseEntity<ResponseWrapper> invalidStudentException(InvalidStudentException exception) {
        var wrapper = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_STUDENT, HttpStatus.BAD_REQUEST);
        log.error("The retrieving the student is failed due to invalid student id. Error message: {}",
                exception.getMessage());
        return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
    }
    /**
     * This method handle student exception response
     *
     * @param exception student exception
     * @return ErrorResponse/InternalServerError
     */
    @ExceptionHandler(StudentException.class)
    public ResponseEntity<ResponseWrapper> studentException(StudentException exception) {
        var wrapper = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("The student service is failed. Error message: {}", exception.getMessage());
        return new ResponseEntity<>(wrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
