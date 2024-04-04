package com.sparking.service_impl;

import com.sparking.entities.data.Field;
import com.sparking.entities.data.ManagerField;
import com.sparking.entities.payloadReq.ManagerFieldPayload;
import com.sparking.repository.ManagerFieldRepo;
import com.sparking.repository.ManagerRepo;
import com.sparking.service.ManagerFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerFieldService_Impl implements ManagerFieldService {

    @Autowired
    ManagerFieldRepo managerFieldRepo;

    @Autowired
    ManagerRepo managerRepo;

    @Override
    public List<ManagerField> createAndUpdate(ManagerFieldPayload managerFieldPayload) {
        List<ManagerField> oldManagerFields = managerFieldRepo.findAll().stream()
                .filter(managerField -> managerField.getManagerId().equals(managerFieldPayload.getManagerId()))
                .collect(Collectors.toList());
        List<Integer> oldFieldIdListOfThisMn = oldManagerFields.stream()
                .map(ManagerField::getFieldId).collect(Collectors.toList());
        List<Integer> newFieldIdListOfThisMn =managerFieldPayload.getFieldId();
//        System.out.println(oldManagerFields);
//        System.out.println(oldFieldIdListOfThisMn);
//        System.out.println(newFieldIdListOfThisMn);
        newFieldIdListOfThisMn.forEach(fieldId -> {
            if(!oldFieldIdListOfThisMn.contains(fieldId)){ // tao moi
                System.out.println();
                managerFieldRepo.createAndUpdate(ManagerField.builder()
                        .fieldId(fieldId)
                        .managerId(managerFieldPayload.getManagerId())
                        .id(0)
                        .lastTimeSetup(new Timestamp(new Date().getTime()))
                        .build());
            }
        });

        for (int i =0; i<oldManagerFields.size(); i++){
            if(!newFieldIdListOfThisMn.contains(oldFieldIdListOfThisMn.get(i))){
                managerFieldRepo.delete(oldManagerFields.get(i).getId());
            }
        }
        return managerFieldRepo.findAll().stream()
                .filter(managerField -> managerField.getManagerId().equals(managerFieldPayload.getManagerId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(int id) {
        return managerFieldRepo.delete(id);
    }

    @Override
    public List<ManagerField> findAll() {
        return managerFieldRepo.findAll();
    }

//    public ManagerField payload2Data(ManagerFieldPayload managerFieldPayload){
//        return ManagerField.builder()
//                .id(managerFieldPayload.getId())
//                .lastTimeSetup(new Timestamp(new Date().getTime()))
//                .fieldId(managerFieldPayload.getFieldId())
//                .managerId(managerFieldPayload.getManagerId())
//                .build();
//    }
}
