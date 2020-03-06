package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.infrastructure.APIRequestClient;
import com.interswitch.smartmoveserver.model.User;
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
import java.util.Map;

/**
 * @author adebola.owolabi
 */
@Service
public class PassportService {

    @Value("${spring.security.oauth2.client.provider.passport.user-info-uri}")
    private String userUrl;
    @Value("${spring.security.oauth2.client.provider.passport.token-uri}")
    private String tokenUrl;
    @Value("${spring.security.oauth2.client.registration.passport.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.passport.client-secret}")
    private String clientSecret;

    @Autowired
    APIRequestClient apiRequestClient;

    public User createUser(User user){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAccessToken());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        return apiRequestClient.Process(user, headers, null, userUrl, HttpMethod.POST, User.class).getBody();
    }

    public User getUserByUsername(String username){
        Object[] params = new Object[]{username};
        ResponseEntity response = apiRequestClient.Process(null, new HttpHeaders(),  params, userUrl, HttpMethod.GET, User.class);
        return (User) response.getBody();
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
        formData.add("username", "adebola.owolabi");
        formData.add("password", "Skimchy678");
        formData.add("grant_type", "password");
        formData.add("scope", "profile");
        ResponseEntity response = apiRequestClient.Process(formData, headers, null, tokenUrl, HttpMethod.POST, Object.class);
        Map<String, Object> resultToJson = (Map<String, Object>) response.getBody();
        return "Bearer " + resultToJson.get("access_token").toString();
    }
}
