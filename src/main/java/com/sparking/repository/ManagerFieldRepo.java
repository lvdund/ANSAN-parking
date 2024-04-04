package com.sparking.repository;

import com.sparking.entities.data.ManagerField;

import java.util.List;

public interface ManagerFieldRepo {
    ManagerField createAndUpdate(ManagerField managerField);

    boolean delete(int id);

    List<ManagerField> findAll();
}
