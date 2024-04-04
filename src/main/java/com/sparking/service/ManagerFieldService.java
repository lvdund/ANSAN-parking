package com.sparking.service;

import com.sparking.entities.data.ManagerField;
import com.sparking.entities.payloadReq.ManagerFieldPayload;

import java.util.List;

public interface ManagerFieldService {

    List<ManagerField> createAndUpdate(ManagerFieldPayload managerFieldPayload);

    boolean delete(int id);

    List<ManagerField> findAll();

}
