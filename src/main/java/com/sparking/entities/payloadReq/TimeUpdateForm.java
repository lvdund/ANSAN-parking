package com.sparking.entities.payloadReq;

import lombok.Data;

@Data
public class TimeUpdateForm {

    int contractId;
    String timeCarIn;
    String timeCarOut;
    
}
