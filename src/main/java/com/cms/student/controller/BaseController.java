package com.cms.student.controller;

import com.cms.student.domain.response.ResponseDto;
import com.cms.student.enums.ErrorResponseStatus;
import com.cms.student.enums.SuccessResponseStatus;
import com.cms.student.wrapper.ErrorResponseWrapper;
import com.cms.student.wrapper.ResponseWrapper;
import com.cms.student.wrapper.SuccessResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The base controller for generate response
 */
public class BaseController {
    /**
     * This method generate successful response
     *
     * @param statusMessage success response message
     * @param data          data
     * @param httpStatus    http status
     * @return Success Response
     */
    public ResponseEntity<ResponseWrapper> getSuccessResponse(SuccessResponseStatus statusMessage,
                                                              ResponseDto data, HttpStatus httpStatus) {
        var wrapper = new SuccessResponseWrapper(statusMessage, data, httpStatus);
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    /**
     * This method generate error response
     *
     * @param statusMessage error response message
     * @return Error Response
     */
    public ResponseEntity<ResponseWrapper> getErrorResponse(ErrorResponseStatus statusMessage) {
        var wrapper = new ErrorResponseWrapper(statusMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method generate internal server error response
     *
     * @return InternalServerError Response
     */
    public ResponseEntity<ResponseWrapper> getInternalServerErrorResponse() {
        var wrapper = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(wrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
