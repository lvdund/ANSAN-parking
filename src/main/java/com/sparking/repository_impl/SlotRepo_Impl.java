package com.sparking.repository_impl;

import com.sparking.entities.data.Field;
import com.sparking.entities.data.Manager;
import com.sparking.entities.data.ManagerField;
import com.sparking.entities.data.Slot;
import com.sparking.entities.payloadReq.CreateNewSlotPayload;
import com.sparking.helpers.HandleSlotID;
import com.sparking.repository.FieldRepo;
import com.sparking.repository.SlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class, timeout = 30000)
@Repository
public class SlotRepo_Impl implements SlotRepo {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    FieldRepo fieldRepo;


    @Override
    public Slot createAndUpdate(Slot slot) {
        int newSlotId = HandleSlotID.handleSlotId(slot.getFieldId(), slot.getId());
        slot.setId(newSlotId);
        return entityManager.merge(slot);
    }

    @Override
    public Slot updateSlotDataCam(Slot slot) {
        return entityManager.merge(slot);
    }

    @Override
    public boolean delete(int id) {
        Slot slot = entityManager.find(Slot.class, id);
        if(slot != null){

            entityManager.remove(slot);
            return true;
        }else {
            return false;
        }
    }

    // --------------------- Admin ------------------

    @Override
    public List<Slot> getByQuantityAndField(String field, String quantity) {
        int query = Integer.parseInt(quantity);
        List<Slot> slots = entityManager.createQuery("Select s from Slot s where s.fieldId =: fieldId")
                .setParameter("fieldId", Integer.parseInt(field)).setMaxResults(query).getResultList();
        return slots;
    }

    @Override
    public List<Slot> getByField(String field) {
//        Date date = new Date();
//        System.out.println("PrevDate - " + date);
        List<Slot> slots = entityManager.createQuery("select s from Slot s where s.fieldId =: fieldId")
                .setParameter("fieldId", Integer.parseInt(field)).getResultList();
//        if (slots.size() > 0) {
//            Date newDate = new Date();
//            System.out.println("NextDate - " + newDate);
//        }
        return slots;
    }

    @Override
    public List<Slot> findAll() {
        return entityManager.createQuery("select s from Slot s").getResultList();
    }

    @Override
    public List<Slot> getSlotByQuantity(String quantity) {
        return entityManager.createQuery("Select s from Slot s")
                .setMaxResults(Integer.parseInt(quantity))
                .getResultList();
    }

    @Override
    public Slot createNewSlot(CreateNewSlotPayload createNewSlotPayload) {
        int slotIdField = createNewSlotPayload.getSlotIdField();
        int fieldId = createNewSlotPayload.getFieldId();
        Slot newSlot;

        int slotId = HandleSlotID.handleSlotId(fieldId, slotIdField);
        Slot slot = findById(slotId);
        if (slot == null) {
            newSlot = Slot.builder()
                    .id(slotId)
                    .fieldId(fieldId)
                    .build();
            entityManager.merge(newSlot);
        } else {
            return null;
        }
        return newSlot;
    }

    @Override
    public Slot updateSlot(Slot slot) {
        int updateSlotId = HandleSlotID.handleSlotId(slot.getFieldId(), slot.getId());

        List<Slot> slots = entityManager.createQuery("select s from Slot s where s.id =: id")
                .setParameter("id", updateSlotId).getResultList();
        if (slots.size() == 1) {
            Slot updateSlot = slots.get(0);
            updateSlot.setStatusCam(slot.getStatusCam());
            updateSlot.setStatusDetector(slot.getStatusDetector());
            return updateSlot;
        } else {
            return null;
        }
    }

    // ---------------------- Manager ---------------------------

    @Override
    public List<Slot> mnGetSlotByQuery(String email, String field, String quantity) {
        List<Slot> arraySlots = findManagerField(email);

        List<Slot> slotByFields = arraySlots.stream()
                .filter(currentSlot -> currentSlot.getFieldId() == Integer.parseInt(field))
                .limit(Integer.parseInt(quantity)).collect(Collectors.toList());

//        System.out.println("SlotByFields - " + slotByFields);
        return slotByFields;
    }

    @Override
    public List<Slot> mnGetSlotByField(String email, String field) {
        List<Slot> arraySlots = findManagerField(email);

        List<Slot> slotByFields = arraySlots.stream()
                .filter(currentSlot -> currentSlot.getFieldId() == Integer.parseInt(field))
                .collect(Collectors.toList());
        return slotByFields;
    }

    @Override
    public List<Slot> mnGetSlotByQuantity(String email, String quantity) {
        List<Slot> arraySlots = findManagerField(email);

        List<Slot> slotByQuantity = arraySlots.stream()
                .limit(Integer.parseInt(quantity))
                .collect(Collectors.toList());
        return slotByQuantity;
    }

    @Override
    public List<Slot> mnGetAllSlot(String email) {
        return findManagerField(email);
    }

    @Override
    public Slot mnCreateNewSlot(String email, CreateNewSlotPayload createNewSlotPayload) {
        int fieldId = createNewSlotPayload.getFieldId();
        int slotIdField = createNewSlotPayload.getSlotIdField();

        List<Slot> arraySlots = findManagerField(email);
        List<Slot> slotByFields = new ArrayList<>();
        Slot newSlot;

        for (Slot currentSlot: arraySlots) {
            if (currentSlot.getFieldId() == fieldId) {
                slotByFields.add(currentSlot);
            } else {
                continue;
            }
        }

        if (slotByFields.size() != 0) {
//            System.out.println("Slot By Field - " + slotByFields);

            boolean isExist = false;
            int createSlotId = HandleSlotID.handleSlotId(fieldId, slotIdField);
            for (Slot currentSlot: slotByFields) {
                if (currentSlot.getId() == createSlotId) {
                    isExist = true;
                    break;
                }
            }
            if (isExist) {
                return null;
            } else {
                newSlot = Slot.builder()
                        .id(createSlotId)
                        .fieldId(fieldId)
                        .build();
                entityManager.merge(newSlot);
                return newSlot;
            }
        }
        return null;
    }

    @Override
    public Slot mnUpdateSlot(Slot slot) {
        int fieldId = slot.getFieldId();
        int slotIdField = HandleSlotID.handleSlotId(fieldId, slot.getId());

        List<Slot> slots = entityManager.createQuery("Select s from Slot s where s.fieldId =: fieldId and s.id =: slotId")
                .setParameter("fieldId", fieldId).setParameter("slotId", slotIdField).getResultList();

        if (slots.size() == 1) {
            Slot updateSlot = slots.get(0);
            updateSlot.setStatusCam(slot.getStatusCam());
            updateSlot.setStatusDetector(slot.getStatusDetector());
            return updateSlot;
        } else {
            return null;
        }
    }

    // ----------------------------------------------------------

    @Override
    public Slot findById(int id){
        return entityManager.find(Slot.class, id);
    }

    @Override
    public Slot managerCreateAndUpdate(Manager manager, Slot slot) {
        if(check(manager, slot)){
            return createAndUpdate(slot);
        }else{
            return null;
        }
    }

    @Override
    public boolean managerDelete(Manager manager, int id) {
        Slot slot = findById(id);
        if(slot == null){
            return false;
        }
        if(check(manager, slot)){
            return delete(id);
        }else{
            return false;
        }
    }

    boolean check(Manager manager, Slot slot){
        List<Field> fieldsOfThisManager = fieldRepo.managerFind(manager);
        for (Field field : fieldsOfThisManager){
            if (field.getId().equals(slot.getFieldId())){
                return true;
            }
        }
        return false;
    }

    List<Slot> findManagerField(String email) {
        List<Manager> managers = entityManager.createQuery("Select m from Manager m where m.email =: email")
                .setParameter("email", email).getResultList();
        if (managers.size() == 1) {
            Manager manager = managers.get(0);
            int managerId = manager.getId();
            List<Slot> arraySlots = new ArrayList<>();

            List<ManagerField> managerFields = entityManager.createQuery("Select m from ManagerField m where m.managerId =: id")
                    .setParameter("id", managerId).getResultList();

            for (ManagerField managerField: managerFields) {
                List<Slot> slots = entityManager.createQuery("Select s from Slot s where s.fieldId =: id")
                        .setParameter("id", managerField.getFieldId()).getResultList();
                for (Slot slot: slots) {
                    arraySlots.add(slot);
                }
            }
            return arraySlots;
        } else {
            return null;
        }
    }
}
