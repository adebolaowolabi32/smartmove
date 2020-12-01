package com.interswitch.smartmoveserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.infrastructure.APIRequestClient;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.PassportUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author adebola.owolabi
 */
@Slf4j
@Service
public class PassportService {

    @Value("${spring.application.passport.user-url}")
    private String userUrl;
    @Value("${spring.application.passport.token-url}")
    private String tokenUrl;
    @Value("${spring.security.oauth2.client.registration.passport.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.passport.client-secret}")
    private String clientSecret;

    @Autowired
    APIRequestClient apiRequestClient;

    public PassportUser createUser(User user){
        PassportUser passportUser = buildUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAccessToken());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        return retrievePassportUser(apiRequestClient.Process(passportUser, headers, null, userUrl, HttpMethod.POST, Object.class).getBody());
    }

    public PassportUser updateUser(User user){
        PassportUser passportUser = buildUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAccessToken());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        return retrievePassportUser(apiRequestClient.Process(passportUser, headers, null, userUrl, HttpMethod.PUT, Object.class).getBody());
    }

    public PassportUser findUser(String username){
        Map<String, String> variables = new HashMap<>();
        variables.put("username", username);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers,  variables, userUrl + "/{username}", HttpMethod.GET, Object.class);
        return retrievePassportUser(response.getBody());
    }

    public String getAccessToken(){
        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authHeader);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
        MultiValueMap formData = new LinkedMultiValueMap();
        formData.add("grant_type", "client_credentials");
        formData.add("scope", "profile");
        ResponseEntity response = apiRequestClient.Process(formData, headers, null, tokenUrl, HttpMethod.POST, Object.class);
        Map<String, Object> resultToJson = (Map<String, Object>) response.getBody();
        return "Bearer " + resultToJson.get("access_token").toString();
    }

    public PassportUser buildUser(User user){
        PassportUser passportUser = new PassportUser();
        passportUser.setUsername(user.getUsername());
        passportUser.setEmail(user.getEmail());
        passportUser.setFirstName(user.getFirstName());
        passportUser.setLastName(user.getLastName());
        passportUser.setMobileNo(user.getMobileNo());
        passportUser.setPassword(user.getPassword());
        passportUser.setEnabled(user.isEnabled());
        passportUser.setFirstLogin(true);
        return passportUser;
    }

    public User buildUser(PassportUser passportUser){
        User user = new User();
        user.setUsername(passportUser.getUsername());
        user.setEmail(passportUser.getEmail());
        user.setFirstName(passportUser.getFirstName());
        user.setLastName(passportUser.getLastName());
        user.setMobileNo(passportUser.getMobileNo());
        user.setPassword(null);
        return user;
    }

    private PassportUser retrievePassportUser(Object response){
        ObjectMapper mapper = new ObjectMapper();
        PassportUser passportUser = mapper.convertValue(response, PassportUser.class);
        if(passportUser.getUsername() == null)
            return null;
        return passportUser;
    }
}
