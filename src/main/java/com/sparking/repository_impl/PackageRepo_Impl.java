package com.sparking.repository_impl;

import com.sparking.entities.data.MyPackage;
import com.sparking.repository.PackageRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(rollbackFor = Exception.class, timeout = 30000)
public class PackageRepo_Impl implements PackageRepo {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public MyPackage create(MyPackage myPackage) {
        return entityManager.merge(myPackage);
    }

    @Override
    public List<MyPackage> findAll(String quantity) {
        return entityManager.createQuery("select x from MyPackage x").setMaxResults(Integer.parseInt(quantity)).getResultList();
    }

    @Override
    public List<MyPackage> getAll() {
        return entityManager.createQuery("Select x from MyPackage x").getResultList();
    }
}
