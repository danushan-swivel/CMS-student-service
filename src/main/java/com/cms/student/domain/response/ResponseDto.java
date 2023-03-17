package com.cms.student.domain.response;

import com.cms.student.exception.StudentException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ResponseDto {
    public String toLogJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new StudentException("Convert object to string is failed", e);
        }
    }
}
