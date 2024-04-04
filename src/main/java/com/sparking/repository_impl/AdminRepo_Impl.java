package com.sparking.repository_impl;

import com.sparking.entities.data.Admin;
import com.sparking.entities.payloadReq.LoginForm;
import com.sparking.repository.AdminRepo;
import com.sparking.security.SHA256Service;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional(rollbackFor = Exception.class, timeout = 30000)
@Repository
public class AdminRepo_Impl implements AdminRepo {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public boolean login(LoginForm loginForm) {
        List<Admin> admins = entityManager
                .createQuery("select a from Admin a where a.email= :email and a.pass = :password")
                .setParameter("email", loginForm.getEmail())
                .setParameter("password", SHA256Service.getSHA256(loginForm.getPassword()))
                .getResultList();
        return admins.size() != 0;
    }

    @Override
    public Admin findByEmail(String email) {
        List<Admin> admins = entityManager
                .createQuery("select a from Admin a where a.email= :email").setParameter("email", email)
                .getResultList();
        if(admins.size() == 1){
            return admins.get(0);
        }
        return null;
    }
}
