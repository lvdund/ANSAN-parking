package com.sparking.helpers;

import java.util.HashMap;

public class ResponseForTags {
    private String email;
    private int userId;

    public ResponseForTags(String email, int userId) {
        this.email = email;
        this.userId = userId;
    }

    public HashMap<String, String> response() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", this.email);
        map.put("id", Integer.toString(this.userId));
        return map;
    }
}
