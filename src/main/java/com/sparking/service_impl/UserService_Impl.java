package com.sparking.service_impl;

import com.sparking.common.Utils;
import com.sparking.entities.data.Contract;
import com.sparking.entities.data.Field;
import com.sparking.entities.data.User;
import com.sparking.entities.jsonResp.FieldJson;
import com.sparking.entities.payloadReq.*;
import com.sparking.repository.BlackListRepo;
import com.sparking.repository.ContractRepo;
import com.sparking.repository.FieldRepo;
import com.sparking.repository.UserRepo;
import com.sparking.security.SHA256Service;
import com.sparking.service.ContractService;
import com.sparking.service.FieldService;
import com.sparking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService_Impl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService_Impl.class);

    @Autowired
    UserRepo userRepo;

    @Autowired
    FieldRepo fieldRepo;

    @Autowired
    ContractRepo contractRepo;

    @Autowired
    BlackListRepo blackListRepo;

    @Autowired
    FieldService fieldService;

    @Autowired
    ContractService contractService;

    @Value("${timeConditionsToOrder}")
    String timeConditionsToOrder;

    @Override
    public User login(LoginForm loginForm) {
        return userRepo.login(loginForm);
    }

    @Override
    public boolean register(RegisterForm registerForm) {
        return userRepo.register(registerForm);
    }

    @Override
    public User createAndUpdate(UserPayload userPayload) {
        userPayload.setPassword(SHA256Service.getSHA256(userPayload.getPassword()));
        return userRepo.createAndUpdate(payload2Data(userPayload));
    }

    @Override
    public boolean delete(int id) {
        return userRepo.delete(id);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Contract park(ParkPayload parkPayload, String email) {
        final long timeExpiredBooking = 3600 * 1000;
        try {
            User user = userRepo.findByEmail(email);
            if(user == null) {
                logger.debug("User Not Found");
                throw new Exception("User Not Found ...");
            }
            System.out.println("User - " + user);
            String equipment = parkPayload.getEquipment();
            if (!user.getEquipment().equals(equipment)) {
                throw new IOException("Equipment Invalid ...");
            }

            System.out.println("Equipment - " + (user.getEquipment().equals(equipment)));
            int userId = user.getId();
            List<Contract> contracts = contractRepo.getContractByUserId(userId);

            System.out.println("ListContract - " + contracts);

            if (contracts.size() < 0 ) {
                return null;
            }
            Contract lastContract = contracts.get(contracts.size() - 1);

            double timeCarIn = lastContract.getTimeCarIn().getTime();
            double timeInBook = lastContract.getTimeInBook().getTime();

            System.out.println("timeCarIn - " + timeCarIn);
            System.out.println("timeInBook - " + timeInBook);

            if (!(lastContract.getTimeCarIn() == null)) {
                throw new Exception("User was not pre-booking after parking ...");
            } else if ((timeCarIn < timeInBook) ||
                    (timeCarIn - timeInBook >= timeExpiredBooking)
            ) {
                throw new Exception("Time booking was expired ...");
            } else {
                logger.info("User parking ...");
                return userRepo.park(parkPayload, lastContract);
            }
        } catch (Exception e) {
            logger.error("Parking went wrong ...");
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public Contract book(BookPayload bookPayload, String email) {
        try {
            User user = userRepo.findByEmail(email);
            if (user == null){
                return null;
            }
            Field field = fieldRepo.findById(bookPayload.getFieldId());
            if (field == null) {
                throw new Exception("Cannot find Field");
            }
            int idArea = field.getIdArea();
            FieldJson fieldJson = fieldService.data2Json(
                    new Field(
                            bookPayload.getFieldId(),"","","","","",50000.0,"",new BigDecimal("0.0"), "", idArea)
            );
            logger.info(fieldJson.toString());

            if (fieldJson.getTotalSlot() > fieldJson.getBusySlot()/2 + fieldJson.getTotalBook()
                    && bookPayload.getTimeInBook().getTime() < bookPayload.getTimeOutBook().getTime()
                    && bookPayload.getTimeInBook().getTime() - new Timestamp(new Date().getTime()).getTime() >= Integer.parseInt(timeConditionsToOrder)// 30 minute
            ) {
                logger.info("userRepository Booking ... ");
                return userRepo.book(bookPayload, user);
            } else {
                throw new IOException("Invalid timeBooking, Please Pre Order booking 30 minutes");
            }
        } catch (Exception e) {
            logger.error("Booking went wrong ...");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean changePass(ChangePassForm changePassForm, String email) {
        User user = userRepo.findByEmail(email);
        return user != null && userRepo.changePass(changePassForm, user);
    }

    public User payload2Data(UserPayload userPayload){
        return User.builder()
                .id(userPayload.getId())
                .address(userPayload.getAddress())
                .idNumber(userPayload.getIdNumber())
                .equipment(userPayload.getEquipment())
                .email(userPayload.getEmail())
                .password(userPayload.getPassword())
                .lastTimeAccess(null)
                .phone(userPayload.getPhone())
                .birth(userPayload.getBirth())
                .image(userPayload.getImage())
                .sex(userPayload.getSex())
                .build();
    }

    @Override
    public boolean verifyAccount(String mail, String code) {
        return userRepo.verifyAccount(mail, code);
    }

    @Override
    public Contract updateTime(TimeUpdateForm timeUpdateForm, String email) throws ParseException {
        User user = userRepo.findByEmail(email);
        List<Contract> contracts = contractRepo.findAll().stream()
                .filter(contract -> (
                            contract.getId()== timeUpdateForm.getContractId()
                        ))
                .collect(Collectors.toList());
        if(contracts.size() == 0){
            return null;
        }
        Contract contract = contracts.get(0);
        if(!contract.getUserId().equals(user.getId())){
            return null;
        }
        List<Field> fields = fieldRepo.findAll().stream()
                .filter(field -> (field.getId().equals(contract.getFieldId())))
                .collect(Collectors.toList());
        if(fields.size() == 0){
            return null;
        }

        if(Utils.getTime(timeUpdateForm.getTimeCarIn()) != null){
            contract.setTimeCarIn(Utils.getTime(timeUpdateForm.getTimeCarIn()));
        }
        if(Utils.getTime(timeUpdateForm.getTimeCarOut()) != null){
            contract.setTimeCarOut(Utils.getTime(timeUpdateForm.getTimeCarOut()));
        }
        // luc dau dang là V

        // xe vao
        if(Utils.getTime(timeUpdateForm.getTimeCarIn()) != null && Utils.getTime(timeUpdateForm.getTimeCarOut()) == null){
            contract.setStatus("Y");
        }
        if(Utils.getTime(timeUpdateForm.getTimeCarOut()) != null){
            contract.setStatus("R");
            double cost = contractService.getCost(contract.getTimeCarIn(), contract.getTimeCarOut()
                    , contract.getTimeCarIn(), contract.getTimeCarOut(), fields.get(0).getPrice());
            contract.setCost(String.valueOf(cost));
        }
        System.out.println(contract);
        return contractRepo.createAndUpdate(contract);
    }

    @Override
    public List<Contract> getListContract(String email) {
        User user = userRepo.findByEmail(email);
        if(user == null){
            return null;
        }
        return contractRepo.findAll().stream()
                .filter(contract -> (contract.getUserId().equals(user.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Contract updateContractForUser(ContractPayload contractPayload, String email) {
        User user = userRepo.findByEmail(email);
        boolean isValidateContract = contractRepo.findAll().stream()
                .filter(contract1 -> (contract1.getId().equals(contractPayload.getId())))
                .count() != 0;
        if(!isValidateContract
                || user == null
                || !contractPayload.getUserId().equals(user.getId())
                || contractPayload.getTimeInBook().getTime() >= contractPayload.getTimeOutBook().getTime()){
            return null;
        }
        return contractRepo.createAndUpdate(contractService.payload2data(contractPayload));
    }

    @Override
    public User updateInfo(UserUpdateInfo userUpdateInfo, String email) {
        User user = userRepo.findByEmail(email);
        return userRepo.createAndUpdate(User.builder()
                .id(user.getId())
                .birth(userUpdateInfo.getBirth())
                .image(userUpdateInfo.getImage())
                .sex(userUpdateInfo.getSex())
                .phone(userUpdateInfo.getPhone())
                .address(userUpdateInfo.getAddress())
                .idNumber(userUpdateInfo.getIdNumber())
                .email(user.getEmail())
                .password(user.getPassword())
                .equipment(userUpdateInfo.getEquipment())
                .lastTimeAccess(user.getLastTimeAccess())
                .build());
    }

    @Override
    public boolean verifyResetPass(VerifyResetPassPayload verifyResetPassPayload) {
        return userRepo.verifyResetPass(verifyResetPassPayload);
    }

    @Override
    public boolean resetPass(String email) {
        return userRepo.resetPass(email);
    }

}
