package com.sparking.repository;

import com.sparking.entities.data.Manager;
import com.sparking.entities.data.Slot;
import com.sparking.entities.jsonResp.SlotJson;
import com.sparking.entities.payloadReq.CreateNewSlotPayload;

import java.util.List;

public interface SlotRepo {

    Slot createAndUpdate(Slot slot);

    Slot updateSlotDataCam(Slot slot);

    boolean delete(int id);

    // -------------- Admin ---------------

    List<Slot> getByQuantityAndField(String field, String quantity);

    List<Slot> getByField(String field);

    List<Slot> findAll();

    List<Slot> getSlotByQuantity(String quantity);

    Slot createNewSlot(CreateNewSlotPayload createNewSlotPayload);

    Slot updateSlot(Slot slot);

    // --------------- Manager ---------------

    List<Slot> mnGetSlotByQuery(String email, String field, String quantity);

    List<Slot> mnGetSlotByField(String email, String field);

    List<Slot> mnGetSlotByQuantity(String email, String quantity);

    List<Slot> mnGetAllSlot(String email);

    Slot mnCreateNewSlot(String email, CreateNewSlotPayload createNewSlotPayload);

    Slot mnUpdateSlot(Slot slot);

    // ----------------------------------------

    Slot findById(int id);

    Slot managerCreateAndUpdate(Manager manager, Slot slot);

    boolean managerDelete(Manager manager, int id);

}
