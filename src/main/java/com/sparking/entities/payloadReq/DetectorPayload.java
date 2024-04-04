package com.sparking.entities.payloadReq;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetectorPayload {

    private Integer id;
    private String addressDetector;
    private Integer slotId;
    private Integer gatewayId;
    private String batteryLevel;
    private String loracomLevel;
    private String operatingMode;
    private String lastTimeSetup;
}
