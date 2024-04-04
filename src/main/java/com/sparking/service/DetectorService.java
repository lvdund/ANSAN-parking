package com.sparking.service;

import com.sparking.entities.data.Detector;
import com.sparking.entities.payloadReq.DetectorPayload;
import com.sparking.entities.payloadReq.UpdateSlotIdPayload;

import java.util.List;

public interface DetectorService {

    Detector createDetector(DetectorPayload detectorPayload);

    boolean deleteDetector(Integer id);

    Detector createAndUpdate(DetectorPayload detectorPayload);

    boolean delete(int id);

    List<Detector> findAll();

    List<Detector> findByGateway(String gateway);

    List<Detector> managerFind(String email);

    List<Detector> managerGetByGateway(String email, String gateway);

    Detector managerCreateAndUpdate(DetectorPayload detectorPayload, String phone);

    Detector updateSlotId(UpdateSlotIdPayload updateSlotIdPayload);

    boolean managerDelete(int id, String phone);

    Detector findById(int id);

    Detector managerFindById(int id, String phone);
}
