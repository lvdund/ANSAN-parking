package com.sparking.entities.payloadReq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ManagerPayload {

    private Integer id;
    private String email;
    private String pass;
    private Boolean acp;

    private String address ;

    private String phone;

    private String image;

    private String sex;

    @JsonFormat(pattern = "YYYY-MM-DD")
    private Date birth;

    int idNumber;

}
