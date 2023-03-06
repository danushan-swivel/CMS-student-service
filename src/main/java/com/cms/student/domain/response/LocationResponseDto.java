package com.cms.student.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponseDto extends ResponseDto{
    private String locationId;
    private String address;
    private String district;
    private String province;
    private long createdAt;
    private long updatedAt;
    private boolean isDeleted;
}
