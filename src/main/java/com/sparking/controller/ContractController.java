package com.sparking.controller;

import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.entities.payloadReq.ContractPayload;
import com.sparking.security.JWTService;
import com.sparking.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class ContractController {

    @Autowired
    ContractService contractService;

    @Autowired
    JWTService jwtService;

    @PostMapping("api/ad/contract/create_and_update")
    public ResponseEntity<Object> createAndUpdate(@RequestBody ContractPayload contractPayload){
        return ResponseEntity.ok(MyResponse.success(contractService.createAndUpdate(contractPayload)));
    }

    @GetMapping("api/ad/contract/find_all")
    public ResponseEntity<Object> findAll(@RequestParam(required = false) String t1,
                                          @RequestParam(required = false) String t2,
                                          @RequestParam(value = "quantity", required = false) String quantity) throws ParseException {
        if(t1!= null && t2 != null){
            return ResponseEntity.ok(MyResponse.success(contractService.findByTime(t1, t2)));
        }
        if (quantity != null) {
            return ResponseEntity.ok(MyResponse.success(contractService.findByQuantity(quantity)));
        }
        return ResponseEntity.ok((MyResponse.success(contractService.findAll())));
    }

    @GetMapping("api/mn/contract/find_all")
    public ResponseEntity<Object> findAllMn(@RequestHeader String token){
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(contractService.managerFind(email)));
    }

    @DeleteMapping("api/ad/contract/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id){
        return ResponseEntity.ok(MyResponse.success(contractService.delete(id)));
    }

}
