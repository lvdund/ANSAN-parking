package com.sparking.entities.payloadReq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;

@Data
public class RegisterForm {

    private String email;
    private String password;
    private String rePassword;
    private Integer idNumber;
    private String equipment;
    private String address;
    private String phone;
    private String image;
    private String sex;
    @JsonFormat(pattern="YYYY-MM-DD")
    private Date birth;
}
