package com.sparking.controller;

import com.sparking.entities.data.User;
import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.entities.payloadReq.*;
import com.sparking.repository.ContractRepo;
import com.sparking.repository.UserRepo;
import com.sparking.security.JWTService;
import com.sparking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    JWTService jwtService;

    @Autowired
    ContractRepo contractRepo;

    @PostMapping("api/public/register")
    public ResponseEntity<Object> register(@RequestBody RegisterForm registerForm){
        if(!registerForm.getPassword().equals(registerForm.getRePassword())){
            return ResponseEntity.ok(MyResponse.fail("password is not equal with rePassword"));
        }
        return ResponseEntity.ok(MyResponse.success(userService.register(registerForm)));
    }

    @PostMapping("api/ad/user/create_and_update")
    public ResponseEntity<Object> createAndUpdate(@RequestBody UserPayload userPayload){
        return ResponseEntity.ok(MyResponse.success(userService.createAndUpdate(userPayload)));
    }

    @GetMapping("api/ad/user/find_all")
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok(MyResponse.success(userService.findAll()));
    }

    @DeleteMapping("api/ad/user/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id){
        return ResponseEntity.ok(MyResponse.success(userService.delete(id)));
    }

    @PostMapping("api/us/book")
    public ResponseEntity<Object> book(@RequestBody BookPayload bookPayload, @RequestHeader String token) throws Exception{
        String email = jwtService.decode(token);
//        System.out.println(bookPayload.getTimeInBook());
        return ResponseEntity.ok(MyResponse.success(userService.book(bookPayload, email)));
    }

    @PostMapping("api/us/parking")
    public ResponseEntity<Object> park(@RequestBody ParkPayload parkPayload, @RequestHeader String token) {
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(userService.park(parkPayload, email)));
    }

    @PostMapping("api/us/changePass")
    public ResponseEntity<Object> changePass(@RequestBody ChangePassForm changePassForm, @RequestHeader String token){
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(userService.changePass(changePassForm, email)));
    }

    @PostMapping("api/public/verify")
    public ResponseEntity<Object> verify(@RequestBody VerifyAccountPayload verifyPayload){
        return ResponseEntity.ok(MyResponse.success(userService.verifyAccount(verifyPayload.getEmail(), verifyPayload.getCode())));
    }

    @PostMapping("api/us/update_time")
    public ResponseEntity<Object> updateTime(@RequestBody TimeUpdateForm timeUpdateForm, @RequestHeader String token) throws ParseException {
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(userService.updateTime(timeUpdateForm, email)));
    }

    @GetMapping("api/us/get_list_contract")
    public ResponseEntity<Object> getListContract(@RequestHeader String token) {
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(userService.getListContract(email)));
    }

    @PostMapping("api/us/update_contract")
    public ResponseEntity<Object> updateContract(@RequestBody ContractPayload contractPayload, @RequestHeader String token) {
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(userService.updateContractForUser(contractPayload, email)));
    }

    @PostMapping("api/us/update_info")
    public ResponseEntity<Object> updateInfo(@RequestBody UserUpdateInfo userUpdateInfo, @RequestHeader String token) {
        String email = jwtService.decode(token);
        return ResponseEntity.ok(MyResponse.success(userService.updateInfo(userUpdateInfo, email)));
    }

    @PostMapping("api/public/verify_reset_pass")
    public ResponseEntity<Object> resetPass(@RequestBody VerifyResetPassPayload verifyResetPassPayload) {
        return ResponseEntity.ok(MyResponse.success(userService.verifyResetPass(verifyResetPassPayload)));
    }

    @PostMapping("api/public/reset_pass")
    public ResponseEntity<Object> resetPass(@RequestBody String email) {
        return ResponseEntity.ok(MyResponse.success(userService.resetPass(email)));
    }

    @GetMapping("api/us/info")
    public ResponseEntity<Object> getInfo(@RequestHeader String token){
        String email = jwtService.decode(token);
        User user = userRepo.findByEmail(email);
        if(user != null){
            return ResponseEntity.ok(MyResponse.success(user));
        }
        return ResponseEntity.ok(MyResponse.fail("invalidate token"));
    }
}
