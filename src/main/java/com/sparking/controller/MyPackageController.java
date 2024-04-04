package com.sparking.controller;

import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.repository.PackageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyPackageController {

    @Autowired
    PackageRepo packageRepo;

    @GetMapping(value = {"api/ad/package/find_all", "api/mn/package/find_all"})
    public ResponseEntity<Object> findAll(@RequestParam(required = false) String quantity){
        if (quantity != null) {
            return ResponseEntity.ok(MyResponse.success(packageRepo.findAll(quantity)));
        }
        return ResponseEntity.ok(MyResponse.success(packageRepo.getAll()));
    }
}
