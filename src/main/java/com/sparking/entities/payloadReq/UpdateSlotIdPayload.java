package com.sparking.entities.payloadReq;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateSlotIdPayload {
    private String addressDetector;
    private Integer gatewayId;
    private Integer slotId;
}
