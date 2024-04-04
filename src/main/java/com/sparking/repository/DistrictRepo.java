package com.sparking.repository;

import com.sparking.entities.data.District;

import java.util.List;

public interface DistrictRepo {
    List<District> getAllDistrict();

    District getDistrictByID(int district);
}
