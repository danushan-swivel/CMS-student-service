package com.cms.student.wrapper;

import com.cms.student.domain.response.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseWrapper {
    private String message;
    private int statusCode;
    private ResponseDto data;
}
