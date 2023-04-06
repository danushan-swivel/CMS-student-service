package com.cms.student.wrapper;

import com.cms.student.enums.ErrorResponseStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponseWrapper extends ResponseWrapper {
    public ErrorResponseWrapper(ErrorResponseStatus responseStatus, HttpStatus httpStatus) {
        super(responseStatus.getMessage(), httpStatus.value(), null);
    }
}
