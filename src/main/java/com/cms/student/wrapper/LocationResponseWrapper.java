package com.cms.student.wrapper;

import com.cms.student.domain.response.LocationResponseDto;
import com.cms.student.domain.response.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseWrapper {
    private String message;
    private int statusCode;
    private LocationResponseDto data;
}
