package com.sparking.entities.payloadReq;

import com.sparking.entities.data.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginPayload {
    private String token;
    private User user;
}
