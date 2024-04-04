package com.sparking.controller;

import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.entities.payloadReq.*;
import com.sparking.security.JWTService;
import com.sparking.service.ManagerService;
import org.apache.http.client.fluent.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ManagerController {
    @Autowired
    ManagerService managerService;

    @Autowired
    JWTService jwtService;

    @PostMapping("api/mn/update_info")
    public ResponseEntity<Object> updateInfo(@RequestBody ManUpdateInfoPayload manUpdateInfo, @RequestHeader String token) {
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(managerService.updateInfo(manUpdateInfo, email)));
    }
    @PostMapping("api/mn/verify_reset_pass")
    public ResponseEntity<Object> resetPass(@RequestBody VerifyResetPassPayload verifyResetPassPayload) {
        return ResponseEntity.ok(MyResponse.success(managerService.verifyResetPass(verifyResetPassPayload)));
    }

    @PostMapping("api/mn/reset_pass")
    public ResponseEntity<Object> resetPass(@RequestBody String email) {
        return ResponseEntity.ok(MyResponse.success(managerService.resetPass(email)));
    }

    @PostMapping("api/mn/changePass")
    public ResponseEntity<Object> changePass(@RequestBody ChangePassForm changePassForm, @RequestHeader String token){
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(managerService.changePass(changePassForm, email)));
    }

    @PostMapping("api/ad/manager/create_and_update")
    public ResponseEntity<Object> createAndUpdate(@RequestBody ManagerPayload managerPayload){
        return ResponseEntity.ok(MyResponse.success(managerService.createAndUpdate(managerPayload)));
    }

    @GetMapping(value = {"api/ad/manager/find_all"})
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok(MyResponse.success(managerService.findAll()));
    }

    @GetMapping("api/mn/info")
    public ResponseEntity<Object> currentManager(@RequestHeader String token) {
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(managerService.currentManager(email)));
    }

    @DeleteMapping("api/ad/manager/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id){
        return ResponseEntity.ok(MyResponse.success(managerService.delete(id)));
    }

//    @PostMapping("api/mn/tags")
//    public ResponseEntity<Object> managerRegisterTagForUser(@RequestBody RegisterTagsPayload registerTagsPayload) {
//        //
//    }

}
