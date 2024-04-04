package com.sparking.repository;

import com.sparking.entities.data.Contract;
import com.sparking.entities.data.User;
import com.sparking.entities.payloadReq.*;

import java.util.List;

public interface UserRepo {

    boolean checkEmailExisted(String email);

    User login(LoginForm loginForm);

    boolean register(RegisterForm registerForm);

    User findById(int id);

    User findByEmail(String email);

    User createAndUpdate(User user);

    boolean delete(int id);

    List<User> findAll();

    Contract book(BookPayload bookPayload, User user);

    boolean changePass(ChangePassForm changePassForm, User user);

    boolean verifyAccount(String mail, String code);

    boolean resetPass(String email);

    boolean verifyResetPass(VerifyResetPassPayload verifyResetPassPayload);

    Contract park(ParkPayload parkPayload, Contract contract);

    User findByTagId(String tagId);

    User findById(Integer userId);
}
