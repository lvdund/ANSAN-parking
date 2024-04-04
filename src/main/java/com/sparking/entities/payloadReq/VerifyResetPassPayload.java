package com.sparking.entities.payloadReq;

import lombok.Data;

@Data
public class VerifyResetPassPayload {

    private String email;
    private String code;
    private String pass;
}
