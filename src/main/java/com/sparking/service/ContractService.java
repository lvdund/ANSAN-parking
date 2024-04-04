package com.sparking.service;

import com.sparking.entities.data.Contract;
import com.sparking.entities.data.Field;
import com.sparking.entities.payloadReq.ContractPayload;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;


public interface ContractService {

    Contract createAndUpdate(ContractPayload contractPayload);

    boolean delete(int id);

    List<Contract> findAll();

    List<Contract> findByQuantity(String quantity);

    Contract payload2data(ContractPayload contractPayload);

    double getCost(Timestamp timeCarin, Timestamp timeCarOut, Timestamp timeBookIn, Timestamp timeBookOut, double price);

    List<Contract> findByTime(String t1, String t2) throws ParseException;

    List<Contract> findByTime(Timestamp t1, Timestamp t2) throws ParseException;

    List<Contract> managerFind(String email);

    List<Contract> findByField(int fieldId);

    List<Contract> findByFieldTime(Timestamp timestamp, Timestamp timestamp1, int fieldId);
}
