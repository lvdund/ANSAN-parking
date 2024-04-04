package com.sparking.security;

import java.security.MessageDigest;


public class SHA256Service {

    public static String getSHA256(String password) {
        String rs = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            rs = bytesToHex(md.digest());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return result.toString();
    }
}
