package com.sparking.repository;

import com.sparking.entities.data.Manager;
import com.sparking.entities.payloadReq.ChangePassForm;
import com.sparking.entities.payloadReq.LoginForm;
import com.sparking.entities.payloadReq.VerifyResetPassPayload;

import java.util.List;

public interface ManagerRepo {

    Manager createAndUpdate(Manager manager);

    boolean delete(int id);

    List<Manager> findAll();

    Manager currentManager(String email);

    Manager login(LoginForm loginForm);

    Manager findByEmail(String email);

    Manager findById(int id);

    boolean changePass(ChangePassForm changePassForm, Manager man);

    boolean resetPass(String email);

    boolean verifyResetPass(VerifyResetPassPayload verifyResetPassPayload);
}
