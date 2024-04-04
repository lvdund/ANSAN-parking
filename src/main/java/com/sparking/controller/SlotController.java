package com.sparking.controller;

import com.sparking.entities.data.Slot;
import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.entities.payloadReq.CreateNewSlotPayload;
import com.sparking.security.JWTService;
import com.sparking.service.SlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class SlotController {

    private static Logger logger = LoggerFactory.getLogger(SlotController.class);

    @Autowired
    SlotService slotService;

    @Autowired
    JWTService jwtService;

    @PostMapping("api/ad/slot/create_and_update")
    public ResponseEntity<Object> createAndUpdate(@RequestBody Slot slot){
        return ResponseEntity.ok(MyResponse.success(slotService.createAndUpdate(slot)));
    }

    @PostMapping("api/mn/slot/create_and_update")
    public ResponseEntity<Object> updateMan(@RequestBody Slot slot ,@RequestHeader String token){
        logger.info(slot.toString());
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(slotService.managerCreateAndUpdate(email, slot)));
    }

    @GetMapping(value = {"api/ad/slot/find_all"})
    public ResponseEntity<Object> findAll(
            @RequestParam(value = "field", required = false) final String field,
            @RequestParam(value = "quantity", required = false) final String quantity){
        if (field != null && Integer.parseInt(field) > 0) {
            if (quantity != null) {
                return ResponseEntity.ok(MyResponse.success(slotService.getByQuantity(field, quantity)));
            }
            return ResponseEntity.ok(MyResponse.success(slotService.getAll(field)));
        }
        return ResponseEntity.ok(MyResponse.success(slotService.findAll()));
    }

    @GetMapping(value = {"api/mn/slot/find_all"})
    public ResponseEntity<Object> mnFindAll(@RequestHeader String token,
                                            @RequestParam(value = "field", required = false) final String field,
                                            @RequestParam(value = "quantity", required = false) final String quantity){
        if (field != null && Integer.parseInt(field) > 0) {
            if (quantity != null && Integer.parseInt(quantity) > 0) {
                return ResponseEntity.ok(MyResponse.success(slotService.mnGetByQuantity(token, field, quantity)));
            }
            return ResponseEntity.ok(MyResponse.success(slotService.mnGetAll(token, field)));
        }
        return null;
    }

    // ------------------Admin-------------------------


    @GetMapping(value = "api/ad/slots")
    public ResponseEntity<Object> findAllSlot(@RequestParam(value = "field", required = false) final String field,
                                              @RequestParam(value = "quantity", required = false) final String quantity) {
        if (field != null && Integer.parseInt(field) > 0) {
            if (quantity != null && Integer.parseInt(quantity) > 0) {
                return ResponseEntity.ok(MyResponse.success(slotService.getSlotByQuantityAndField(field, quantity)));
            }
            return ResponseEntity.ok(MyResponse.success(slotService.getSlotByFieldId(field)));
        }
        if (quantity != null && field == null && Integer.parseInt(quantity) > 0) {
            return ResponseEntity.ok(MyResponse.success(slotService.getSlotByQuantity(quantity)));
        }
        return ResponseEntity.ok(MyResponse.success(slotService.getAllSlot()));
    }

    @PostMapping(value = "api/ad/slots")
    public ResponseEntity<Object> createSlots(@RequestBody CreateNewSlotPayload createNewSlotPayload) {
        return ResponseEntity.ok(MyResponse.success(slotService.createNewSlot(createNewSlotPayload)));
    }

    @PutMapping(value = "api/ad/slots")
    public ResponseEntity<Object> updateSlots(@RequestBody Slot slot) {
        return ResponseEntity.ok(MyResponse.success(slotService.updateSlot(slot)));
    }

    @DeleteMapping("api/ad/slots/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id){
        return ResponseEntity.ok(MyResponse.success(slotService.delete(id)));
    }



    // -------------Manager---------------------

    @GetMapping("api/mn/slots")
    public ResponseEntity<Object> managerFindSlot(@RequestHeader String token,
                                                  @RequestParam(value = "field", required = false) final String field,
                                                  @RequestParam(value = "quantity", required = false) final String quantity) {
        String email = jwtService.decode(token);
        if (field != null && Integer.parseInt(field) > 0) {
            if (quantity != null && Integer.parseInt(quantity) > 0) {
                return ResponseEntity.ok(MyResponse.success(slotService.mnGetSlotByQuery(email, field, quantity)));
            }
            return ResponseEntity.ok(MyResponse.success(slotService.mnGetSlotByField(email, field)));
        }
        if (quantity != null && Integer.parseInt(quantity) > 0) {
            return ResponseEntity.ok(MyResponse.success(slotService.mnGetSlotByQuantity(email, quantity)));
        }
        return ResponseEntity.ok(MyResponse.success(slotService.mnGetAllSlot(email)));
    }

    @PostMapping(value = "api/mn/slots")
    public ResponseEntity<Object> managerCreateSlot(@RequestHeader String token, @RequestBody CreateNewSlotPayload createNewSlotPayload) {
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(slotService.mnCreateNewSlot(email, createNewSlotPayload)));
    }

    @PutMapping(value = "api/mn/slots")
    public ResponseEntity<Object> managerUpdateSlot(@RequestBody Slot slot) {
        System.out.println(slot);
        return ResponseEntity.ok(MyResponse.success(slotService.mnUpdateSlot(slot)));
    }

    @DeleteMapping("api/mn/slots/{id}")
    public ResponseEntity<Object> deleteMan(@PathVariable int id, @RequestHeader String token){
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(slotService.managerDelete(email, id)));
    }


    // -----------------------------------------------------------------------------------------

    @GetMapping(value = {"api/public/slot/find_by_id/{id}"})
    public ResponseEntity<Object> findById(@PathVariable Integer id){
        return ResponseEntity.ok(MyResponse.success(slotService.findById(id)));
    }

}
