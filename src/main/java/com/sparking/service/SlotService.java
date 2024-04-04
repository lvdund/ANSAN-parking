package com.sparking.service;

import com.sparking.entities.data.Slot;
import com.sparking.entities.jsonResp.SlotJson;
import com.sparking.entities.payloadReq.CreateNewSlotPayload;

import java.util.List;

public interface SlotService {

    SlotJson createAndUpdate(Slot slot);

    boolean delete(int id);

    List<SlotJson> getByQuantity(String field, String quantity);

    List<SlotJson> getAll(String field);

    List<SlotJson> findAll();

    List<SlotJson> mnGetByQuantity(String token, String field, String quantity);

    List<SlotJson> mnGetAll(String email, String field);

    // ----------- Admin ------------

    List<Slot> getSlotByQuantityAndField(String field, String quantity);

    List<Slot> getSlotByFieldId(String field);

    List<Slot> getSlotByQuantity(String quantity);

    List<Slot> getAllSlot();

    Slot createNewSlot(CreateNewSlotPayload createNewSlotPayload);

    Slot updateSlot(Slot slot);

    // ----------- Manager ---------------

    List<Slot> mnGetSlotByQuery(String email, String field, String quantity);

    List<Slot> mnGetSlotByField(String email, String field);

    List<Slot> mnGetSlotByQuantity(String email, String quantity);

    List<Slot> mnGetAllSlot(String email);

    Slot mnCreateNewSlot(String email, CreateNewSlotPayload createNewSlotPayload);

    Slot mnUpdateSlot(Slot slot);

    // -------------------------------------------

    SlotJson findById(int id);

    SlotJson managerCreateAndUpdate(String email, Slot slot);

    boolean managerDelete(String email, int id);
}
