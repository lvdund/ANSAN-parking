package com.sparking.entities.jsonResp;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class SlotJson {

    private Integer id;
    private Integer fieldId;
    private Boolean statusDetector; // false la free, true la busy
    private Boolean statusCam;
    private String AddressGateway;
    private String AddressDetector;
    private Timestamp lastTimeCam;
    private Timestamp lastTimeDetector;
    private Integer detectorId;

}
