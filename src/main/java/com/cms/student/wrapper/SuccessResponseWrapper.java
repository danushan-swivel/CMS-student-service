package com.cms.student.wrapper;

import com.cms.student.domain.response.ResponseDto;
import com.cms.student.enums.SuccessResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponseWrapper extends ResponseWrapper {
    public SuccessResponseWrapper(SuccessResponseStatus responseStatus, ResponseDto data) {
        super(responseStatus.getMessage(), responseStatus.getStatusCode(), data);
    }
}
