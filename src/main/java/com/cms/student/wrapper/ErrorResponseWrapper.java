package com.cms.student.wrapper;

import com.cms.student.domain.response.ResponseDto;
import com.cms.student.enums.ErrorResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseWrapper extends ResponseWrapper{
    public ErrorResponseWrapper(ErrorResponseStatus responseStatus, ResponseDto data) {
        super(responseStatus.getMessage(), responseStatus.getStatusCode(), data);
    }
}
