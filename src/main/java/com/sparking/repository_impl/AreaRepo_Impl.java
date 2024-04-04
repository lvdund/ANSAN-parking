package com.sparking.repository_impl;

import com.sparking.entities.data.Area;
import com.sparking.repository.AreaRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AreaRepo_Impl implements AreaRepo {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Area> getAllAreas() {
        return entityManager.createQuery("Select a from Area a").getResultList();
    }

    @Override
    public Area getAreaById(int area) {
        List<Area> areas = entityManager.createQuery("Select a from Area a where a.id =:area").setParameter("area", area).getResultList();
        return areas.get(0);
    }
}
