package com.sparking.service_impl;

import com.sparking.entities.payloadReq.LoginForm;
import com.sparking.repository.AdminRepo;
import com.sparking.repository.BlackListRepo;
import com.sparking.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService_Impl implements AdminService {

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    BlackListRepo blackListRepo;

    @Override
    public boolean login(LoginForm loginForm) {
        return adminRepo.login(loginForm);
    }

}
