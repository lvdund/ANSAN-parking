package com.sparking.repository;

import com.sparking.entities.data.Admin;
import com.sparking.entities.payloadReq.LoginForm;

public interface AdminRepo {
    boolean login(LoginForm loginForm);
    Admin findByEmail(String email);
}
