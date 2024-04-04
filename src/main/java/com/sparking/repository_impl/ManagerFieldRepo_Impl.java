package com.sparking.repository_impl;

import com.sparking.entities.data.ManagerField;
import com.sparking.repository.ManagerFieldRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional(rollbackFor = Exception.class, timeout = 30000)
@Repository
public class ManagerFieldRepo_Impl implements ManagerFieldRepo {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public ManagerField createAndUpdate(ManagerField managerField) {
        return entityManager.merge(managerField);
    }

    @Override
    public boolean delete(int id) {
        ManagerField managerField = entityManager.find(ManagerField.class, id);
        if(managerField != null){
            entityManager.remove(managerField);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<ManagerField> findAll() {
        return entityManager.createQuery("select x from ManagerField x").getResultList();
    }
}
