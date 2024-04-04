package com.sparking.repository;

import com.sparking.entities.data.Contract;
import com.sparking.entities.data.User;

import java.util.List;

public interface ContractRepo {

    Contract createAndUpdate(Contract contract);

    boolean delete(int id);

    List<Contract> findAll();

    List<Contract> findByQuantity(String quantity);

    List<Contract> findByUser(User user);

    List<Contract> findByField(int fieldId);

    List<Contract> getContractByUserId(int userId);

//    List<Contract>  findBySlotId(int slotId);

}
