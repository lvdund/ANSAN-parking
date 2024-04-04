package com.sparking.repository_impl;

import com.sparking.entities.data.StatsField;
import com.sparking.repository.StatsFieldRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class, timeout = 30000)
@Repository
public class StatsFieldRepo_Impl implements StatsFieldRepo {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public StatsField createAndUpdate(StatsField statsField) {
       return entityManager.merge(statsField);
    }

    @Override
    public List<StatsField> getLatest() {
        return entityManager.createQuery("Select s from StatsField s").getResultList();
    }

    @Override
    public List<StatsField> findByTime(long since, long until) {
        List<StatsField> statsFields = getLatest();

        return statsFields.stream().filter(statsField -> {
            if((statsField.getDay().getTime()<since)||(statsField.getDay().getTime()>until))
                return false;
            else
                return true;
        }).collect(Collectors.toList());
    }

    @Override
    public List<StatsField> findByFiledTime(long since, long until, int fieldId) {
        List<StatsField> statsFields = getLatest();

        return statsFields.stream().filter(statsField -> {
            if((statsField.getDay().getTime()<since)||(statsField.getDay().getTime()>until)||(statsField.getFieldId()!=fieldId))
                return false;
            else{
             //   System.out.println("DEBUG statsFields: " + statsField + " " + fieldId);
                return true;}
        }).collect(Collectors.toList());
    }
}
