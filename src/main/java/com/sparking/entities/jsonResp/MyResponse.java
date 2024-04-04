package com.sparking.entities.jsonResp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyResponse {

    private String message;
    private Object data;

    public static MyResponse success(Object data){
        return new MyResponse("success", data);
    }

    public static MyResponse fail(Object data){
        return new MyResponse("fail", data);
    }

    public static MyResponse loginSuccess(String role,Object data){
        return new MyResponse(role, data);
    }
}
