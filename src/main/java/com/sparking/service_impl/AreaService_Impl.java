package com.sparking.service_impl;

import com.sparking.entities.data.Area;
import com.sparking.repository.AreaRepo;
import com.sparking.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService_Impl implements AreaService {
    @Autowired
    private AreaRepo areaRepo;

    @Override
    public List<Area> getAllAreas() {
        return areaRepo.getAllAreas();
    }

}
