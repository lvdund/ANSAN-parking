package com.sparking.service_impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparking.entities.data.User;
import com.sparking.entities.social.GooglePojo;
import com.sparking.entities.social.GooglePojoIdToken;
import com.sparking.getData.HttpResponse;
import com.sparking.repository.UserRepo;

import com.sparking.service.SocialService;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
public class GoogleService implements SocialService {

    @Autowired
    private UserRepo userRepo;

    @Value("${google.link.access_token}")
    private String linkAccessToken;

    @Value("${google.link.id_token}")
    private String linkIdToken;

    @Value("${googleKeyAPI}")
    private String googleKey;

    @Override
    public User getUserAccessToken(String token) throws IOException {
        String link = linkAccessToken + token; // tạo link api
        String response = Request.Get(link).execute().returnContent().asString(); // call api
        System.out.println("google access token response: " + response);
        ObjectMapper mapper = new ObjectMapper();
        GooglePojo pojo = mapper.readValue(response, GooglePojo.class); // map với entity
        String email = pojo.getEmail();
        User user = userRepo.findByEmail(email);
        if (user != null) return user;
        User newUser = buildUser(email);
        return userRepo.createAndUpdate(newUser);
    }

    @Override
    public User getUserIdToken(String token) throws IOException {
        String link = linkIdToken + token;
        String response = Request.Get(link).execute().returnContent().asString(); // call api
        System.out.println("google id token response: " + response);
        ObjectMapper mapper = new ObjectMapper();
        GooglePojoIdToken pojo = mapper.readValue(response, GooglePojoIdToken.class); // map với entity
        String email = pojo.getEmail();
        if(pojo.getEmail_verified().equals("true")){
            User user = userRepo.findByEmail(email);
            if (user != null){
                return user;
            }else {
                User newUser = buildUser(email);
                return userRepo.createAndUpdate(newUser);
            }
        }
        return null;
    }




    @Override
    public String getPlaceDetails(Map<String, String> allParams) {
        String uri = "https://maps.googleapis.com/maps/api/place/details/json?";
        return getGoogleMapApi(allParams,uri);
    }

    @Override
    public String getPlaceAutocomplete(Map<String, String> allParams) {
        String uri = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
        return getGoogleMapApi(allParams,uri);

    }

    @Override
    public String getDirection(Map<String, String> allParams) {
        String uri = "https://maps.googleapis.com/maps/api/directions/json?";
        return getGoogleMapApi(allParams,uri);
    }

    public User buildUser(String email){
        return User.builder()
                .id(null)
                .lastTimeAccess(null)
                .password("")
                .equipment("")
                .idNumber(0)
                .address("")
                .email(email)
                .address("")
                .phone("")
                .sex("")
                .image("")
                .birth(new Date())
                .build();
    }

    public String getGoogleMapApi(Map<String, String> allParams, String uri) {
        // String uri = "https://maps.googleapis.com/maps/api/directions/json?";

        for (Map.Entry<String, String> entry : allParams.entrySet())
        {
            System.out.println(entry.getKey() + " " + entry.getValue());
            uri += entry.getKey() + "=" + entry.getValue().replaceAll(" ", "+") + "&";
        }

        uri+="key=" + googleKey;
      //  System.out.println("SEND HTTP GET "+ uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet= new HttpGet(uri);

        // httpGet.addHeader("X-M2M-Origin",originator);
        // httpGet.addHeader("Accept","application/json");

        HttpResponse httpResponse = new HttpResponse();

        try {
            CloseableHttpResponse closeableHttpResponse = httpclient.execute(httpGet);
            try{
                httpResponse.setStatusCode(closeableHttpResponse.getStatusLine().getStatusCode());
                httpResponse.setBody(EntityUtils.toString(closeableHttpResponse.getEntity()));
            }finally{
                closeableHttpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       // System.out.println(httpResponse.getBody());
        return httpResponse.getBody();
        //return null;
    }
}
