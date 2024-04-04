package com.sparking.service_impl;

import com.sparking.entities.data.District;
import com.sparking.repository.DistrictRepo;
import com.sparking.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService_Impl implements DistrictService {
    @Autowired
    DistrictRepo districtRepo;

    @Override
    public List<District> getAllDistrict() {
        return districtRepo.getAllDistrict();
    }
}
