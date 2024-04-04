package com.sparking.service;

import com.sparking.entities.data.User;

import java.io.IOException;
import java.util.Map;

public interface SocialService {

    User getUserAccessToken(String token) throws Exception;

    User getUserIdToken(String token) throws IOException;

  //  String getGoogleMapApi(Map<String,String> allParams);

    String getPlaceDetails(Map<String, String> allParams);

    String getPlaceAutocomplete(Map<String, String> allParams);

    String getDirection(Map<String, String> allParams);
}
