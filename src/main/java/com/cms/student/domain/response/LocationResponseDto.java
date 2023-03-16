package com.cms.student.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class LocationResponseDto extends ResponseDto{
    private String locationId;
    private String address;
    private String district;
    private String province;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDeleted;
}
