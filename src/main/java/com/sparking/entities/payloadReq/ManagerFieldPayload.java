package com.sparking.entities.payloadReq;

import lombok.Data;

import java.util.List;

@Data
public class ManagerFieldPayload {

//    private Integer id;
    private List<Integer> fieldId;
    private int managerId;

}
