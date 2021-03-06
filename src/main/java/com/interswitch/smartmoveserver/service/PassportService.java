package com.interswitch.smartmoveserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.infrastructure.APIRequestClient;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.*;
import com.interswitch.smartmoveserver.model.response.PassportErrorResponse;
import com.interswitch.smartmoveserver.model.response.UserPassportResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

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
    @Value("${spring.application.passport.change-password-url}")
    private String changePasswordUrl;
    @Value("${smartmove.url}")
    private String smartMoveBaseUrl;
    @Value("${spring.application.passport.account.recovery}")
    private String resetPasswordUrl;
    @Value("${spring.application.passport.account.recovery-notification}")
    private String resetPasswordNotificationUrl;

    @Autowired
    APIRequestClient apiRequestClient;

    public PassportUser createUser(User user) {
        PassportUser passportUser = buildUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAccessToken());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        try {
            return retrievePassportUser(apiRequestClient.Process(passportUser, headers, null, userUrl, HttpMethod.POST, Object.class).getBody());
        } catch (ResponseStatusException ex) {
            String response = "";
            ObjectMapper mapper = new ObjectMapper();
            PassportErrorResponse passportErrorResponse;
            response = ex.getReason();
            try {
                passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                String message = passportErrorResponse.getDescription();
                if (message == null || message.isEmpty()) {
                    message = "";
                    for (PassportErrorResponse.Error error : passportErrorResponse.getErrors()) {
                        message += error.getMessage() + ". ";
                    }
                }
                throw new ResponseStatusException(ex.getStatus(), message);
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(ex.getStatus(), "Failed to retrieve response.");
            }
        }
    }

    public PassportUser updateUser(User user) {
        PassportUser passportUser = buildUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAccessToken());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        return retrievePassportUser(apiRequestClient.Process(passportUser, headers, null, userUrl, HttpMethod.PUT, Object.class).getBody());
    }

    public PassportUser findUser(String username) {
        Map<String, String> variables = new HashMap<>();
        variables.put("username", username);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAccessToken());
        try {
            ResponseEntity response = apiRequestClient.Process(null, headers, variables, userUrl + "/{username}", HttpMethod.GET, Object.class);
            return retrievePassportUser(response.getBody());
        } catch (ResponseStatusException ex) {
            String response = "";
            ObjectMapper mapper = new ObjectMapper();
            PassportErrorResponse passportErrorResponse;
            response = ex.getReason();
            try {
                passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                String message = passportErrorResponse.getDescription();
                if (message == null || message.isEmpty()) {
                    message = "";
                    for (PassportErrorResponse.Error error : passportErrorResponse.getErrors()) {
                        message += error.getMessage() + ". ";
                    }
                }
                throw new ResponseStatusException(ex.getStatus(), message);
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(ex.getStatus(), "Failed to retrieve response.");
            }

        }
    }

    public UserPassportResponse getUserAccessDetails(UserLoginRequest userLoginRequest) {
        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authHeader);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
        MultiValueMap formData = new LinkedMultiValueMap();
        formData.add("grant_type", "password");
        formData.add("scope", "profile");
        formData.add("username", userLoginRequest.getUsername());
        formData.add("password", userLoginRequest.getPassword());
        UserPassportResponse passportResponse = null;
        ResponseEntity<UserPassportResponse> responseEntity = null;
        try {
            responseEntity = apiRequestClient.Process(formData, headers, null, tokenUrl, HttpMethod.POST, UserPassportResponse.class);
            return responseEntity.getBody();
        } catch (Exception ex) {
            String response = "";
            ObjectMapper mapper = new ObjectMapper();
            PassportErrorResponse passportErrorResponse;
            try {
                if (ex instanceof HttpClientErrorException.Forbidden) {
                    response = ((HttpClientErrorException) ex).getResponseBodyAsString();
                    passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, passportErrorResponse.getDescription());
                }
                if (ex instanceof HttpClientErrorException) {
                    response = ((HttpClientErrorException) ex).getResponseBodyAsString();
                    passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, passportErrorResponse.getDescription());
                } else if (ex instanceof HttpServerErrorException || ex instanceof UnknownHttpStatusCodeException) {
                    response = ((HttpClientErrorException) ex).getResponseBodyAsString();
                    mapper = new ObjectMapper();
                    passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                    throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, passportErrorResponse.getDescription());
                } else if (ex instanceof ResponseStatusException) {
                    response = ((ResponseStatusException) ex).getReason();
                    passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                    String message = passportErrorResponse.getDescription();
                    if (message == null || message.isEmpty()) {
                        message = "";
                        for (PassportErrorResponse.Error error : passportErrorResponse.getErrors()) {
                            message += error.getMessage() + ". ";
                        }
                    }
                    throw new ResponseStatusException(((ResponseStatusException) ex).getStatus(), message);
                }
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to retrieve response.");
            }
            return null;
        }
    }

    public void changePassword(String token, ChangePassword changePassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        try {
            apiRequestClient.Process(changePassword, headers, null, changePasswordUrl, HttpMethod.POST, Object.class).getBody();
        } catch (ResponseStatusException ex) {
            String response = "";
            ObjectMapper mapper = new ObjectMapper();
            PassportErrorResponse passportErrorResponse;
            response = ex.getReason();
            try {
                passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                String message = passportErrorResponse.getDescription();
                if (message == null || message.isEmpty()) {
                    message = "";
                    for (PassportErrorResponse.Error error : passportErrorResponse.getErrors()) {
                        message += error.getMessage() + ". ";
                    }
                }
                throw new ResponseStatusException(ex.getStatus(), message);
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(ex.getStatus(), "Failed to retrieve response.");
            }
        }
    }

    public boolean initiatePasswordReset(String username) {
        //returns 200 OK
        UserAccountRecoveryNotification resetPassword = new UserAccountRecoveryNotification();
        resetPassword.setDestinationUri(smartMoveBaseUrl.concat("/resetpassword"));
        resetPassword.setUsername(username);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAccessToken());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        try {
            return apiRequestClient.Process(resetPassword, headers, null, resetPasswordNotificationUrl, HttpMethod.POST, Object.class).getStatusCode() == HttpStatus.OK;
        } catch (ResponseStatusException ex) {
            String response = "";
            ObjectMapper mapper = new ObjectMapper();
            PassportErrorResponse passportErrorResponse;
            response = ex.getReason();
            try {
                passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                String message = passportErrorResponse.getDescription();
                if (message == null || message.isEmpty()) {
                    message = "";
                    for (PassportErrorResponse.Error error : passportErrorResponse.getErrors()) {
                        message += error.getMessage() + ". ";
                    }
                }
                throw new ResponseStatusException(ex.getStatus(), message);
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to retrieve response.");
            }
        }
    }

    public boolean doPasswordReset(UserAccountRecovery passwordResetBody) {
        //returns 204 OK
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAccessToken());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        boolean passwordResetSuccessful = false;

        try {
            passwordResetSuccessful = apiRequestClient.Process(passwordResetBody, headers, null, resetPasswordUrl, HttpMethod.POST, Object.class).getStatusCode() == HttpStatus.NO_CONTENT;
            return passwordResetSuccessful;
        } catch (Exception ex) {
            String response = "";
            ObjectMapper mapper = new ObjectMapper();
            PassportErrorResponse passportErrorResponse;
            try {
                if (ex instanceof RestClientException) {
                    response = ((HttpClientErrorException) ex).getResponseBodyAsString();
                    passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, passportErrorResponse.getDescription());
                } else if (ex instanceof ResponseStatusException) {
                    response = ((ResponseStatusException) ex).getReason();
                    passportErrorResponse = mapper.readValue(response, PassportErrorResponse.class);
                    String message = passportErrorResponse.getDescription();
                    if (message == null || message.isEmpty()) {
                        message = "";
                        for (PassportErrorResponse.Error error : passportErrorResponse.getErrors()) {
                            message += error.getMessage() + ". ";
                        }
                    }
                    throw new ResponseStatusException(((ResponseStatusException) ex).getStatus(), message);
                }
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to retrieve response.");
            }
            return false;
        }
    }

    public String getAccessToken() {
        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
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

    public PassportUser buildUser(User user) {
        PassportUser passportUser = new PassportUser();
        passportUser.setUsername(user.getUsername());
        passportUser.setEmail(user.getEmail());
        passportUser.setFirstName(user.getFirstName());
        passportUser.setLastName(user.getLastName());
        passportUser.setMobileNo(user.getMobileNo());
        passportUser.setPassword(user.getPassword());
        passportUser.setEnabled(user.isEnabled());
        return passportUser;
    }

    public User buildUser(PassportUser passportUser) {
        User user = new User();
        user.setUsername(passportUser.getUsername());
        user.setEmail(passportUser.getEmail());
        user.setFirstName(passportUser.getFirstName());
        user.setLastName(passportUser.getLastName());
        user.setMobileNo(passportUser.getMobileNo());
        user.setPassword(null);
        return user;
    }

    private PassportUser retrievePassportUser(Object response) {
        ObjectMapper mapper = new ObjectMapper();
        PassportUser passportUser = mapper.convertValue(response, PassportUser.class);
        if (passportUser.getUsername() == null)
            return null;
        return passportUser;
    }
}
