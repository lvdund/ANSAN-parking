package com.sparking.repository_impl;

import com.sparking.entities.data.Contract;

import com.sparking.entities.data.User;
import com.sparking.repository.ContractRepo;
import com.sparking.repository.SlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional(rollbackFor = Exception.class, timeout = 30000)
@Repository
public class ContractRepo_Impl implements ContractRepo {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    SlotRepo slotRepo;

    @Override
    public Contract createAndUpdate(Contract contract) {
//        System.out.println("ContractRepo_Impl.createAndUpdate" + contract);
//        System.out.println("Contract Debug - " + contract.getStatus());
        if(!contract.getStatus().equals("V")// đặt trước
                && !contract.getStatus().equals("Y")// đã thuê
                && !contract.getStatus().equals("C")// đã huỷ
                && !contract.getStatus().equals("R")// đã trả chỗ
                && !contract.getStatus().equals("I")){ // đặt không book trước
            return null;
        }
        return entityManager.merge(contract);
    }

    @Override
    public boolean delete(int id) {
        Contract contract = entityManager.find(Contract.class, id);
        if(contract != null){
            entityManager.remove(contract);
            return true;
        }
        return false;
    }

    @Override
    public List<Contract> findAll() {
        return  entityManager.createQuery("select g from Contract g").getResultList();
    }

    @Override
    public List<Contract> findByQuantity(String quantity) {
        return entityManager.createQuery("Select c from Contract c").setMaxResults(Integer.parseInt(quantity)).getResultList();
    }

    @Override
    public List<Contract> findByUser(User user) {
        Integer userId = user.getId();
        Query query = entityManager
                .createQuery("select u from Contract u where u.userId= :userId");
        List<Contract> contracts = query.setParameter("userId", userId).getResultList();

        return contracts;

    }

    @Override
    public List<Contract> findByField(int fieldId) {
        Query query = entityManager
                .createQuery("select u from Contract u where u.fieldId= :fieldId");
        List<Contract> contracts = query.setParameter("fieldId", fieldId).getResultList();
        return contracts;
    }

    @Override
    public List<Contract> getContractByUserId(int userId) {
        List<Contract> contracts = entityManager.createQuery("Select c from Contract c where c.userId = :userId")
                .setParameter("userId", userId).getResultList();
        return contracts;
    }
}
