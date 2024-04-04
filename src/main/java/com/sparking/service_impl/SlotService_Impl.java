package com.sparking.service_impl;

import com.sparking.entities.data.*;
import com.sparking.entities.jsonResp.FieldJson;
import com.sparking.entities.jsonResp.SlotJson;
import com.sparking.entities.payloadReq.CreateNewSlotPayload;
import com.sparking.repository.*;
import com.sparking.security.JWTService;
import com.sparking.service.FieldService;
import com.sparking.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlotService_Impl implements SlotService {

    @Autowired
    SlotRepo slotRepo;

    @Autowired
    DetectorRepo detectorRepo;

    @Autowired
    GatewayRepo gatewayRepo;

    @Autowired
    DataCamAndDetectorRepo dataCamAndDetectorRepo;

    @Autowired
    ManagerRepo managerRepo;

    @Autowired
    FieldService fieldService;

    @Autowired
    JWTService jwtService;

    @Override
    public SlotJson createAndUpdate(Slot slot) {
        Slot newSlot = slotRepo.createAndUpdate(slot);

//        System.out.println(newSlot);
        return data2Json(newSlot);
    }

    @Override
    public boolean delete(int id) {
        return slotRepo.delete(id);
    }

    @Override
    public List<SlotJson> getByQuantity(String field, String quantity) {
        return slotRepo.getByQuantityAndField(field, quantity).stream().map(this::data2Json).collect(Collectors.toList());
    }

    @Override
    public List<SlotJson> getAll(String field) {
        return slotRepo.getByField(field).stream().map(this::data2Json).collect(Collectors.toList());
    }

    @Override
    public List<SlotJson> findAll() {
        return slotRepo.findAll().stream().map(this::data2Json).collect(Collectors.toList());
    }

    @Override
    public List<SlotJson> mnGetByQuantity(String token, String field, String quantity) {
        List<SlotJson> slotJsons = new ArrayList<>();
        String email = jwtService.decode(token);
        Manager manager = managerRepo.findByEmail(email);
        if(manager == null){
            return null;
        }
        List<Slot> slots = slotRepo.getByQuantityAndField(field, quantity);
        List <FieldJson> fieldJsons = fieldService.managerFind(email);
        for(FieldJson fieldJson : fieldJsons){
            slotJsons.addAll(
                    slots.stream().filter(slot -> slot.getFieldId() == fieldJson.getId()).map(this::data2Json).collect(Collectors.toList())
            );
        }
        return slotJsons;
    }

    @Override
    public List<SlotJson> mnGetAll(String token, String field) {
        List<SlotJson> slotJsons = new ArrayList<>();
        String email = jwtService.decode(token);
        Manager manager = managerRepo.findByEmail(email);
        if(manager == null){
            return null;
        }
        List<Slot> slots = slotRepo.getByField(field);
        List <FieldJson> fieldJsons = fieldService.managerFind(email);
        for(FieldJson fieldJson : fieldJsons){
            slotJsons.addAll(
                    slots.stream().filter(slot -> slot.getFieldId() == fieldJson.getId()).map(this::data2Json).collect(Collectors.toList())
            );
        }
        return slotJsons;
    }

    // ----------- Admin ---------------

    @Override
    public List<Slot> getSlotByQuantityAndField(String field, String quantity) {
        return slotRepo.getByQuantityAndField(field, quantity);
    }

    @Override
    public List<Slot> getSlotByFieldId(String field) {
        return slotRepo.getByField(field);
    }

    @Override
    public List<Slot> getSlotByQuantity(String quantity) {
        return slotRepo.getSlotByQuantity(quantity);
    }

    @Override
    public List<Slot> getAllSlot() {
        return slotRepo.findAll();
    }

    @Override
    public Slot createNewSlot(CreateNewSlotPayload createNewSlotPayload) {
        return slotRepo.createNewSlot(createNewSlotPayload);
    }

    @Override
    public Slot updateSlot(Slot slot) {
        return slotRepo.updateSlot(slot);
    }

    // -------------- Manager -----------------

    @Override
    public List<Slot> mnGetSlotByQuery(String email, String field, String quantity) {
        return slotRepo.mnGetSlotByQuery(email, field, quantity);
    }

    @Override
    public List<Slot> mnGetSlotByField(String email, String field) {
        return slotRepo.mnGetSlotByField(email, field);
    }

    @Override
    public List<Slot> mnGetSlotByQuantity(String email, String quantity) {
        return slotRepo.mnGetSlotByQuantity(email, quantity);
    }

    @Override
    public List<Slot> mnGetAllSlot(String email) {
        return slotRepo.mnGetAllSlot(email);
    }

    @Override
    public Slot mnCreateNewSlot(String email, CreateNewSlotPayload createNewSlotPayload) {
        return slotRepo.mnCreateNewSlot(email, createNewSlotPayload);
    }

    @Override
    public Slot mnUpdateSlot(Slot slot) {
        return slotRepo.mnUpdateSlot(slot);
    }

    // ---------------------------------------

    @Override
    public SlotJson findById(int id) {
        Slot slot = slotRepo.findById(id);
        return slot == null ? null : data2Json(slotRepo.findById(id));
    }

    @Override
    public SlotJson managerCreateAndUpdate(String email, Slot slot) {
        Manager manager = managerRepo.findByEmail(email);
        if(manager == null){
            return null;
        }
        return data2Json(slotRepo.managerCreateAndUpdate(manager, slot));
    }

    @Override
    public boolean managerDelete(String email, int id) {
        Manager manager = managerRepo.findByEmail(email);
        if(manager == null){
            return false;
        }
        return slotRepo.managerDelete(manager,id);
    }

    public SlotJson data2Json(Slot slot){
        List<Detector> detectors = detectorRepo.findBySlotId(slot.getId());
        List<DataCamAndDetector> dataCamAndDetectors = dataCamAndDetectorRepo.findAll().stream()
                .filter(dataCamAndDetector -> (
                        dataCamAndDetector.getSlotId().equals(slot.getId())
                )).collect(Collectors.toList());

        return SlotJson.builder()
                .id(slot.getId())
                .AddressDetector(detectors.size() != 0 ? detectors.get(0).getAddressDetector() : null)
                .AddressGateway(detectors.size() != 0 ? gatewayRepo.findById(detectors.get(0).getGatewayId()).getAddressGateway(): null)
                .fieldId(slot.getFieldId())
                .lastTimeDetector(detectors.size() != 0 ? detectors.get(0).getLastTimeUpdate(): null)
                .lastTimeCam(dataCamAndDetectors.size() !=0 ?dataCamAndDetectors.get(dataCamAndDetectors.size()-1).getTime(): null)
                .statusDetector(slot.getStatusDetector())
                .statusCam(slot.getStatusCam())
                .detectorId(detectors.size() != 0 ? detectors.get(0).getId() : null)
                .build();
    }
}
