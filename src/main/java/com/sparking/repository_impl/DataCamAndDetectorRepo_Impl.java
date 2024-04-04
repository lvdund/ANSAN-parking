package com.sparking.repository_impl;

import com.sparking.entities.data.DataCamAndDetector;
import com.sparking.repository.DataCamAndDetectorRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public class DataCamAndDetectorRepo_Impl implements DataCamAndDetectorRepo {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public DataCamAndDetector createAndUpdate(DataCamAndDetector dataCamAndDetector) {
        return entityManager.merge(dataCamAndDetector);
    }

    @Override
    public boolean delete(int id) {
        DataCamAndDetector dataCamAndDetector = entityManager.find(DataCamAndDetector.class, id);
        if(dataCamAndDetector != null){
            entityManager.remove(dataCamAndDetector);
            return true;
        }
        return false;
    }

    @Override
    public List<DataCamAndDetector> findAll() {
        return entityManager.createQuery("select x from DataCamAndDetector x").getResultList();
    }
}
