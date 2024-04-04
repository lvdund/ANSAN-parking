package com.sparking.service;

public interface SendMailService {
    boolean sendMail(String userMail, String header, String content);
    boolean sendHtmlMail(String userMail, String header, String content);
}
