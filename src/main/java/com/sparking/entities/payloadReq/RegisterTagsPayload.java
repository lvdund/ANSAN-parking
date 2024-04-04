package com.sparking.entities.payloadReq;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterTagsPayload {
    private String email;
    private String tagId;
}
