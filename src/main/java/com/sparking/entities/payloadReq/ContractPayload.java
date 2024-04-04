package com.sparking.entities.payloadReq;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class ContractPayload {

    private Integer id;

    private Integer userId;

    private Integer fieldId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timeInBook;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timeOutBook;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timeCarIn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timeCarOut;

    private String carNumber;

    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private Timestamp dtCreate;

    private String status;
}
