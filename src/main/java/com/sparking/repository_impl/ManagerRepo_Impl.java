package com.sparking.repository_impl;

import com.sparking.entities.data.CodeResetPass;
import com.sparking.entities.data.Manager;
import com.sparking.entities.data.User;
import com.sparking.entities.payloadReq.ChangePassForm;
import com.sparking.entities.payloadReq.LoginForm;
import com.sparking.entities.payloadReq.VerifyResetPassPayload;
import com.sparking.repository.ManagerRepo;
import com.sparking.repository.UserRepo;
import com.sparking.security.SHA256Service;
import com.sparking.service.SendMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = Exception.class, timeout = 30000)
@Repository
public class ManagerRepo_Impl implements ManagerRepo {
private static Logger logger = LoggerFactory.getLogger(ManagerRepo_Impl.class);
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SendMailService sendMailService;

    @Override
    public Manager createAndUpdate(Manager manager) {
        List<Manager> oldManagers = entityManager.createQuery("select x from Manager x where x.id =:id")
                .setParameter("id", manager.getId()).getResultList();

        if(userRepo.checkEmailExisted(manager.getEmail())){
            if(oldManagers.size() == 0
                    || !oldManagers.get(0).getId().equals(manager.getId())
                    || !oldManagers.get(0).getEmail().equals(manager.getEmail())){
                return null;
            }
        }
        manager.setPass(SHA256Service.getSHA256(manager.getPass())); // just update password for manager
        return entityManager.merge(manager);
    }

    @Override
    public boolean delete(int id) {
        Manager manager = entityManager.find(Manager.class, id);
        if(manager != null){

            entityManager.remove(manager);
            return true;
        }
        return false;
    }

    @Override
    public List<Manager> findAll() {
        return entityManager.createQuery("select x from Manager x").getResultList();
    }

    @Override
    public Manager currentManager(String email) {
        Manager manager = findByEmail(email);
        return manager;
    }

    @Override
    public Manager login(LoginForm loginForm) {
        List<Manager> managers = entityManager
                .createQuery("select m from Manager m where m.email= :email and m.pass = :password")
                .setParameter("email", loginForm.getEmail())
                .setParameter("password", SHA256Service.getSHA256(loginForm.getPassword()))
                .getResultList();
        if(managers.size() != 0){
            managers.get(0).setLastTimeAccess(new Timestamp(new Date().getTime()));

            return managers.get(0);
        }
        return null;
    }

    @Override
    public Manager findByEmail(String email) {
        List<Manager> managers = entityManager
                .createQuery("select m from Manager m where m.email= :email").setParameter("email", email).getResultList();
        if(managers.size() == 1){
            return managers.get(0);
        }
        return null;
    }

    @Override
    public Manager findById(int id) {
        return entityManager.find(Manager.class, id);
    }

    @Override
    public boolean changePass(ChangePassForm changePassForm, Manager man) {
        if(changePassForm.getPassword().equals(changePassForm.getRePassword())){
            if((man.getPass() == null && changePassForm.getOldPassword().equals(""))
                    || (SHA256Service.getSHA256(changePassForm.getOldPassword()).equals(man.getPass())
                    && (man.getPass() != null)
            )
            ){
                man.setPass(SHA256Service.getSHA256(changePassForm.getPassword()));
                entityManager.merge(man);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean resetPass(String email) {
        if(findByEmail(email) == null){
            return false;
        }
        String code = getRandomCode();
        boolean b = sendMailService.sendMail(email
                , "Parking space reset password"
                , "To verify reset pass, please enter this code to reset page: " + code);
        if(b) entityManager.merge(CodeResetPass.builder()
                .code(code)
                .email(email)
                .id(null)
                .build());
        return b;
    }

    @Override
    public boolean verifyResetPass(VerifyResetPassPayload verifyResetPassPayload) {
        Manager oldMan = findByEmail(verifyResetPassPayload.getEmail());
        if(oldMan == null){
            return false;
        }
        List<CodeResetPass> codeResetPasses = entityManager
                .createQuery("select x from CodeResetPass x where x.email =: email")
                .setParameter("email", verifyResetPassPayload.getEmail())
                .getResultList();
        if(codeResetPasses.size() != 0
                && codeResetPasses.get(codeResetPasses.size() - 1).getCode().equals(verifyResetPassPayload.getCode())){

            oldMan.setPass(SHA256Service.getSHA256(verifyResetPassPayload.getPass()));
            logger.info(oldMan.toString());
            createAndUpdate(oldMan);
            return true;
        }
        return false;
    }

    public String getRandomCode(){
        String rs = "";
        for (int i = 0; i < 4; i++){
            rs += String.valueOf((int) (Math.random() * 10));
        }
        return rs + new Date().getTime()/1000;
    }
}
