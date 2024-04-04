package com.sparking.entities.payloadReq;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetNewsTagPayload {
    private String timeStart;
    private String timeEnd;
}
