package com.sparking.service;

import com.sparking.entities.data.Manager;
import com.sparking.entities.payloadReq.*;

import java.util.List;

public interface ManagerService {

    Manager createAndUpdate(ManagerPayload managerPayload);

    boolean delete(int id);

    List<Manager> findAll();

    Manager currentManager(String email);

    Manager login(LoginForm loginForm);

    Manager findById(int id);

    boolean changePass(ChangePassForm changePassForm, String email);

    boolean resetPass(String email);

    boolean verifyResetPass(VerifyResetPassPayload verifyResetPassPayload);

    Manager updateInfo(ManUpdateInfoPayload manUpdateInfo, String email);

    // boolean changePass(ChangePassForm changePassForm, String email);
}
