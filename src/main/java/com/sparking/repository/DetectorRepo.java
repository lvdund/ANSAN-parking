package com.sparking.repository;

import com.sparking.entities.data.Detector;
import com.sparking.entities.data.Manager;
import com.sparking.entities.payloadReq.DetectorPayload;
import com.sparking.entities.payloadReq.UpdateSlotIdPayload;

import java.util.List;

public interface DetectorRepo {

    Detector createDetector(DetectorPayload detectorPayload);

    boolean deleteDetector(Integer id);

    Detector createAndUpdate(Detector detector);

    boolean delete(int id);

    List<Detector> findAll();

    List<Detector> findByGateway(String gateway);

    List<Detector> findBySlotId(int id);

    List<Detector> managerFind(Manager manager);

    List<Detector> managerGetByGateway(Manager manager, String gateway);

    Detector managerCreateAndUpdate(Detector detector, Manager manager);

    Detector updateSlotId(UpdateSlotIdPayload updateSlotIdPayload);

    boolean managerDelete(int id, Manager manager);

    Detector findById(int id);

    Detector managerFindById(int id, Manager manager);
}
