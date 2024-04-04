package com.sparking.repository;

import com.sparking.entities.data.Field;
import com.sparking.entities.data.Manager;
import com.sparking.entities.jsonResp.FieldJson;

import java.util.List;
import java.util.Map;

public interface FieldRepo{

    Field createAndUpdate(Field field);

    boolean delete(int id);

    List<Field> findAll();

    List<Field> filterByDistrict(int district);

    List<Field> filterByArea(int area);

    List<Field> managerFind(Manager manager);

    Field managerUpdate(Field field, Manager manager);

    boolean managerDelete(int id, Manager manager);

    Field findById(Integer fieldId);
}
