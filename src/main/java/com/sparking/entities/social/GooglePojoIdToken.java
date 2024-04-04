package com.sparking.entities.social;

import lombok.Data;

@Data
public class GooglePojoIdToken {
    private String issued_to;
    private String audience;
    private String user_id;
    private String expires_in;
    private String email;
    private String email_verified; // true or false
    private String issuer;
    private String issued_at;
}
