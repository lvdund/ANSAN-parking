package com.sparking.repository_impl;

import com.sparking.entities.data.BlackList;
import com.sparking.repository.BlackListRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional(rollbackFor = Exception.class, timeout = 30000)
@Repository
public class BlackListRepo_Impl implements BlackListRepo {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public BlackList create(String token) {
        return entityManager.merge(new BlackList(null, token));
    }

    public List<BlackList> findByToken(String token){
        return entityManager.createQuery("select b from BlackList b where b.token = :token")
        .setParameter("token", token).getResultList();
    }


}
