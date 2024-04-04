package com.sparking.repository;

import com.sparking.entities.data.Area;

import java.util.List;

public interface AreaRepo {
    List<Area> getAllAreas();

    Area getAreaById(int area);
}
