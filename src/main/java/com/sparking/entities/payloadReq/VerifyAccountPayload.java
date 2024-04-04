package com.sparking.entities.payloadReq;

import lombok.Data;

@Data
public class VerifyAccountPayload {
    private String email;
    private String code;
}
