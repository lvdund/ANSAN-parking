package com.sparking.controller;

import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AreaController {
    @Autowired
    private AreaService areaService;

    @GetMapping("api/ad/areas")
    public ResponseEntity<Object> getAllAreas() {
        return ResponseEntity.ok(MyResponse.success(areaService.getAllAreas()));
    }
}
