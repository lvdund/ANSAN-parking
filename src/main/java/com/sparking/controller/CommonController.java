package com.sparking.controller;

import com.sparking.entities.data.Field;
import com.sparking.entities.data.Manager;
import com.sparking.entities.data.User;
import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.entities.payloadReq.LoginForm;
import com.sparking.entities.payloadReq.ManLoginPayload;
import com.sparking.entities.payloadReq.UserLoginPayload;
import com.sparking.repository.AdminRepo;
import com.sparking.repository.FieldRepo;
import com.sparking.repository.ManagerRepo;
import com.sparking.repository.UserRepo;
import com.sparking.security.JWTService;
import com.sparking.service.AdminService;
import com.sparking.service.ManagerService;
import com.sparking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class CommonController {

    @Autowired
    UserService userService;

    @Autowired
    ManagerService managerService;

    @Autowired
    AdminService adminService;

    @Autowired
    JWTService jwtService;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ManagerRepo managerRepo;

    @Autowired
    FieldRepo fieldRepo;


    @PostMapping("api/public/login")
    public ResponseEntity<Object> login(@RequestBody LoginForm loginForm) throws Exception {
        User user =  userService.login(loginForm);
        Manager man = managerService.login(loginForm);
//        System.out.println(loginForm);
        if(user !=null){
            return ResponseEntity.ok(MyResponse
                    .loginSuccess("user",new UserLoginPayload(jwtService.getToken(loginForm.getEmail()),user)));
        }else if(man!=null){
            List<Field> fields = fieldRepo.managerFind(man);
            List<Integer> fieldByMan = new ArrayList<>();

            for(Field field: fields) {
                fieldByMan.add(field.getId());
            }
            return ResponseEntity.ok(MyResponse.loginSuccess("manager",new ManLoginPayload(jwtService.getToken(loginForm.getEmail()),man.getId(),fieldByMan)));

        }else if(adminService.login(loginForm)){
            return ResponseEntity.ok(MyResponse.loginSuccess("admin",jwtService.getToken(loginForm.getEmail())));
        }
        return ResponseEntity.ok(MyResponse.fail("wrong email or password"));
    }
    @GetMapping("api/public/logout")
    public ResponseEntity<Object> logout(@RequestHeader String token){
        String email = jwtService.decode(token);
        if(userRepo.findByEmail(email) != null
                || managerRepo.findByEmail(email) != null
                || adminRepo.findByEmail(email) != null){
            return ResponseEntity.ok(MyResponse.success(true));
        }
        return ResponseEntity.ok(MyResponse.success(false));
    }

    @GetMapping("api/public/get_role")
    public ResponseEntity<Object> getRole(@RequestHeader String token){
        String email = jwtService.decode(token);
        if(userRepo.findByEmail(email) != null){
            return ResponseEntity.ok(MyResponse.success("user"));
        }else if(managerRepo.findByEmail(email) != null){
            return ResponseEntity.ok(MyResponse.success("manager"));
        }else if(adminRepo.findByEmail(email) != null){
            return ResponseEntity.ok(MyResponse.success("admin"));
        }else {
            return ResponseEntity.ok(MyResponse.fail("invalidate token"));
        }
    }


}
