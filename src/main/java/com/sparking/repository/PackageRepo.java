package com.sparking.repository;

import com.sparking.entities.data.MyPackage;

import java.util.List;

public interface PackageRepo {
    MyPackage create(MyPackage myPackage);

    List<MyPackage> findAll(String quantity);

    List<MyPackage> getAll();
}