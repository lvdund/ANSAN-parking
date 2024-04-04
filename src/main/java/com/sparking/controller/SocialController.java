package com.sparking.controller;

import com.sparking.entities.data.User;
import com.sparking.entities.jsonResp.MyResponse;
import com.sparking.entities.payloadReq.UserLoginPayload;
import com.sparking.security.JWTService;
import com.sparking.service.SocialService;
import com.sparking.service_impl.GoogleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@AllArgsConstructor
@Controller
public class SocialController {

    private final JWTService jwtService;
    private final GoogleService googleService;
    private SocialService socialService;

//    @GetMapping("login-google")
//    public String loginGoogle() {
//        System.out.println(googleService.createAuthorizationURL());
//        return String.format("redirect:%s", googleService.createAuthorizationURL());
//    }
//
//    @GetMapping("google-callback")
//    public ResponseEntity<Object> googleCallback(@RequestParam(name = "code") String code) throws Exception {
//        String accessToken = googleService.createAccessToken(code);
//        User user = googleService.getUser(accessToken);
//        System.out.println("code" + code);
//        System.out.println("accessToken" + accessToken);
//        return ResponseEntity.ok(jwtService.getToken(user.getEmail()));
//    }


    @GetMapping("api/directions/json")
    @ResponseBody
    public String getDirection(@RequestParam Map<String,String> allParams){
      //  System.out.println("Params = " + allParams.entrySet());
        return socialService.getDirection(allParams);
    }

    @GetMapping("api/place/autocomplete/json")
    @ResponseBody
    public String getPlaceAutocomplete(@RequestParam Map<String,String> allParams){
     //   System.out.println("Params = " + allParams.entrySet());
        return socialService.getPlaceAutocomplete(allParams);
    }

    @GetMapping("api/place/details/json")
    @ResponseBody
    public String getPlaceDetails(@RequestParam Map<String,String> allParams){
    //    System.out.println("Params = " + allParams.entrySet());
        return socialService.getPlaceDetails(allParams);
    }



    @GetMapping("api/login-google/access-token")
    @ResponseBody
    public ResponseEntity<Object> accessToken(@RequestParam(name = "token") String token) throws Exception {
        User user = googleService.getUserAccessToken(token);
        return ResponseEntity.ok(MyResponse
                .loginSuccess("user",new UserLoginPayload(jwtService.getToken(user.getEmail()),user)));
    }

    @GetMapping("api/login-google/id-token")
    @ResponseBody
    public ResponseEntity<Object> idToken(@RequestParam(name = "token") String token) throws Exception {
        User user = googleService.getUserIdToken(token);
        return ResponseEntity.ok(MyResponse
                .loginSuccess("user",new UserLoginPayload(jwtService.getToken(user.getEmail()),user)));
    }

}
