package com.sparking.controller;

import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.entities.payloadReq.DetectorPayload;
import com.sparking.entities.payloadReq.UpdateSlotIdPayload;
import com.sparking.entities.payloadReq.UpdateSlotIdPayload;
import com.sparking.security.JWTService;
import com.sparking.service.DetectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DetectorController {

    @Autowired
    DetectorService detectorService;

    @Autowired
    JWTService jwtService;

//    @PostMapping("api/ad/detector/create_and_update")
//    public ResponseEntity<Object> createAndUpdate(@RequestBody DetectorPayload detectorPayload){
//        return ResponseEntity.ok(MyResponse.success(detectorService.createAndUpdate(detectorPayload)));
//    }

    @GetMapping("api/ad/detector/find_all")
    public ResponseEntity<Object> findAll(@RequestParam(value = "gwid", required = false) final String gateway){
        if (gateway != null && Integer.parseInt(gateway) > 0) {
            return ResponseEntity.ok(MyResponse.success(detectorService.findByGateway(gateway)));
        }
        return ResponseEntity.ok(MyResponse.success(detectorService.findAll()));
    }

//    @DeleteMapping("api/ad/detector/delete/{id}")
//    public ResponseEntity<Object> delete(@PathVariable int id){
//        return ResponseEntity.ok(MyResponse.success(detectorService.delete(id)));
//    }

    @GetMapping(value = {"api/mn/detector/find_all"})
    public ResponseEntity<Object> managerFindAll(@RequestHeader String token,
                                                 @RequestParam(value = "gwid", required = false) final String gateway){
        String email = jwtService.decode(token);
        if (gateway != null && Integer.parseInt(gateway) > 0) {
            return ResponseEntity.ok(MyResponse.success(detectorService.managerGetByGateway(email, gateway)));
        }
        return ResponseEntity.ok(MyResponse.success(detectorService.managerFind(email)));
    }

//    @PostMapping("api/mn/detector/create_and_update")
//    public ResponseEntity<Object> managerCreateAndUpdate(@RequestBody DetectorPayload detectorPayload
//            , @RequestHeader String token){
//        String phone = jwtService.decode(token);
//        return ResponseEntity.ok(MyResponse.success(detectorService.managerCreateAndUpdate(detectorPayload, phone)));
//    }
//
//    @DeleteMapping("api/mn/detector/delete/{id}")
//    public ResponseEntity<Object> managerDelete(@PathVariable int id, @RequestHeader String token){
//        String phone = jwtService.decode(token);
//        return ResponseEntity.ok(MyResponse.success(detectorService.managerDelete(id, phone)));
//    }

    @GetMapping("api/ad/detector/find_by_id/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id){
        return ResponseEntity.ok(MyResponse.success(detectorService.findById(id)));
    }

    @GetMapping(value = {"api/mn/detector/find_by_id/{id}"})
    public ResponseEntity<Object> managerFindById(@PathVariable Integer id,@RequestHeader String token){
        String phone = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(detectorService.managerFindById(id, phone)));
    }

    @PostMapping("api/ad/detector")
    public ResponseEntity<Object> createDetector(@RequestBody DetectorPayload detectorPayload) {
        return ResponseEntity.ok(MyResponse.success(detectorService.createDetector(detectorPayload)));
    }

    @PutMapping("api/ad/detector")
    public ResponseEntity<Object> UpdateSlotId(
            @RequestBody UpdateSlotIdPayload updateSlotIdPayload
    ) {
        return ResponseEntity.ok(MyResponse.success(detectorService.updateSlotId(updateSlotIdPayload)));
    }

    @DeleteMapping("api/ad/detector/{id}")
    public ResponseEntity<Object> deleteDetector(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.ok(MyResponse.fail("Invalid Detector"));
        }
        return ResponseEntity.ok(MyResponse.success(detectorService.deleteDetector(id)));
    }
}
