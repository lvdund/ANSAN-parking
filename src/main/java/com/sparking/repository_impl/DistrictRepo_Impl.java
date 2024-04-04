package com.sparking.repository_impl;

import com.sparking.entities.data.District;
import com.sparking.repository.DistrictRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DistrictRepo_Impl implements DistrictRepo {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<District> getAllDistrict() {
        return entityManager.createQuery("Select d from District d").getResultList();
    }

    @Override
    public District getDistrictByID(int district) {
        List<District> districts = entityManager
                .createQuery("Select d from District d where d.id =:id").setParameter("id", district).getResultList();
        return districts.get(0);
    }
}
