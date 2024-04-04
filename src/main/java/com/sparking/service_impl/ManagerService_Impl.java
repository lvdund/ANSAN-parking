package com.sparking.service_impl;

import com.sparking.entities.data.Manager;
import com.sparking.entities.data.User;
import com.sparking.entities.payloadReq.*;
import com.sparking.repository.BlackListRepo;
import com.sparking.repository.ManagerRepo;
import com.sparking.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService_Impl implements ManagerService {

    @Autowired
    ManagerRepo managerRepo;

    @Autowired
    BlackListRepo blackListRepo;



    @Override
    public Manager createAndUpdate(ManagerPayload  managerPayload) {
        System.out.println("DEBUG create man" + payload2data(managerPayload));
        return managerRepo.createAndUpdate(payload2data(managerPayload));
    }

    @Override
    public boolean delete(int id) {
        return managerRepo.delete(id);
    }

    @Override
    public List<Manager> findAll() {
        return managerRepo.findAll();
    }

    @Override
    public Manager currentManager(String email) {
        return managerRepo.currentManager(email);
    }

    @Override
    public Manager login(LoginForm loginForm) {
        return managerRepo.login(loginForm);
    }

    @Override
    public Manager findById(int id) {
        return managerRepo.findById(id);
    }

    @Override
    public boolean changePass(ChangePassForm changePassForm, String email) {

            Manager man = managerRepo.findByEmail(email);
            return man != null && managerRepo.changePass(changePassForm, man);
        }

    @Override
    public boolean resetPass(String email) {
        return managerRepo.resetPass(email);
    }

    @Override
    public boolean verifyResetPass(VerifyResetPassPayload verifyResetPassPayload) {
       return managerRepo.verifyResetPass(verifyResetPassPayload);
    }

    @Override
    public Manager updateInfo(ManUpdateInfoPayload manUpdateInfo, String email) {
        Manager man = managerRepo.findByEmail(email);

        //TODO
                //Confg Manager in DB and update info
        return managerRepo.createAndUpdate(Manager.builder()
                .id(man.getId())
                .birth(manUpdateInfo.getBirth())
                .image(manUpdateInfo.getImage())
                .sex(manUpdateInfo.getSex())
                .phone(manUpdateInfo.getPhone())
                .address(manUpdateInfo.getAddress())
                .idNumber(manUpdateInfo.getIdNumber())
                .email(man.getEmail())
                .pass(man.getPass())
                .lastTimeAccess(man.getLastTimeAccess())
                .acp(true) // always set true for active manager
                .build());
    }


    public Manager payload2data(ManagerPayload managerPayload){
        return Manager.builder()
                .id(managerPayload.getId())
                .email(managerPayload.getEmail())
                .acp(managerPayload.getAcp())
                .pass(managerPayload.getPass())
                .address(managerPayload.getAddress())
                .birth(managerPayload.getBirth())
                .image(managerPayload.getImage())
                .phone(managerPayload.getPhone())
                .sex(managerPayload.getSex())
                .idNumber(managerPayload.getIdNumber())
                .lastTimeAccess(null)
                .acp(true) // always set true with active manager
                .build();
    }
}
