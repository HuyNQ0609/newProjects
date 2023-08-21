package com.vsafe.admin.server.business.request.operation;

import lombok.Data;

@Data
public class RepresentativeRequest {
    private String name;
    private String phoneNumber;
    private String email;
    private String identityNumber;
}
