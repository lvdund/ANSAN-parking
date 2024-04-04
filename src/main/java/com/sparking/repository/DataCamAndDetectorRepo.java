package com.sparking.repository;

import com.sparking.entities.data.DataCamAndDetector;

import java.util.List;

public interface DataCamAndDetectorRepo {

    DataCamAndDetector createAndUpdate(DataCamAndDetector dataCamAndDetector);

    boolean delete(int id);

    List<DataCamAndDetector> findAll();
}
