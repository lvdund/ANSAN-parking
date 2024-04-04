package com.sparking.controller;

import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    @GetMapping("api/ad/districts")
    public ResponseEntity<Object> getAllDistrict() {
        return ResponseEntity.ok(MyResponse.success(districtService.getAllDistrict()));
    }

    @GetMapping("api/ad/district/{id}")
    public ResponseEntity<Object> getDistrict() {
        return null;
    }

    @PostMapping("api/ad/district")
    public ResponseEntity<Object> addDistrict(@RequestBody() String body) {
        return null;
    }

    @PutMapping("api/ad/district/{id}")
    public ResponseEntity<Object> updateDistrict(@PathVariable(value = "id") String id) {
        return null;
    }
}
