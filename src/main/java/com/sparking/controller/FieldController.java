package com.sparking.controller;

import com.sparking.entities.data.Field;
import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.security.JWTService;
import com.sparking.service.FieldService;
import org.apache.http.client.fluent.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
public class FieldController {

    @Autowired
    FieldService fieldService;

    @Autowired
    JWTService jwtService;

    @PostMapping("api/ad/field/create_and_update")
    public ResponseEntity<Object> createAndUpdate(@RequestBody Field field){
        return ResponseEntity.ok(MyResponse.success(fieldService.createAndUpdate(field)));
    }

//    /api/ad/analysis?field=1&since=3454764&until=45647564&unit=hour
    @GetMapping("api/ad/analysis")
    public ResponseEntity<Object> analysis(@RequestParam int field, @RequestParam long since,
                                           @RequestParam long until, @RequestParam String unit) throws ParseException{
        return ResponseEntity.ok(MyResponse.success(fieldService.analysis(field, since, until, unit)));
    }

    @GetMapping("api/mn/analysis")
    public ResponseEntity<Object> mnAnalysis(@RequestParam int field, @RequestParam long since, @RequestParam long until,
                                             @RequestParam String unit, @RequestHeader String token) throws ParseException {
        return ResponseEntity.ok(MyResponse.success(fieldService.mnAnalysis(field, since, until, unit, token)));
    }

    @GetMapping(value = {"api/public/field/find_all","api/ad/field/find_all"})// can multiple mapping
    public ResponseEntity<Object> findAll(
                @RequestParam(value = "district", required = false) final String district,
                @RequestParam(value = "area", required = false) final String area
            ) {
        if (district != null) {
            return ResponseEntity.ok(MyResponse.success(fieldService.filterByDistrict(Integer.parseInt(district))));
        } else if (area != null) {
            return ResponseEntity.ok(MyResponse.success(fieldService.filterByArea(Integer.parseInt(area))));
        }
        return ResponseEntity.ok(MyResponse.success(fieldService.findAll()));
    }

    @DeleteMapping("api/ad/field/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id){
        return ResponseEntity.ok(MyResponse.success(fieldService.delete(id)));
    }

    @GetMapping(value = {"api/mn/field/find_all"})
    public ResponseEntity<Object> managerFindAll(@RequestHeader String token){
        String phone = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(fieldService.managerFind(phone)));
    }

    @PostMapping(value = {"api/mn/field/update"})
    public ResponseEntity<Object> managerUpdate(@RequestBody Field field ,@RequestHeader String token){
        String phone = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(fieldService.managerUpdate(field,phone)));
    }

    @DeleteMapping("api/mn/field/delete/{id}")
    public ResponseEntity<Object> managerDelete(@PathVariable int id, @RequestHeader String token){
        String phone = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(fieldService.managerDelete(id, phone)));
    }

}
