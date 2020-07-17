package com.interswitch.smartmoveserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.infrastructure.APIRequestClient;
import com.interswitch.smartmoveserver.model.Scheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * Created by adebola.owolabi on 6/18/2020
 */
@Service
public class SchemeService {
    @Value("${settlement.service.schemes-url}")
    private String schemesUrl;

    @Autowired
    APIRequestClient apiRequestClient;

    @Autowired
    PassportService passportService;

    public Set<Scheme> getSchemes() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, null, schemesUrl, HttpMethod.GET, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        Set<Scheme> schemes = mapper.convertValue(response.getBody(), new TypeReference<Set<Scheme>>() {
        });
        return schemes;
    }

    public Scheme createScheme(Scheme scheme) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(scheme, headers, null, schemesUrl, HttpMethod.POST, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getBody(), Scheme.class);
    }

    public Scheme updateScheme(Scheme scheme) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(scheme, headers, null, schemesUrl, HttpMethod.PUT, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getBody(), Scheme.class);
    }

    public Scheme findScheme(String id) {
        Map<String, String> variables = new HashMap<>();
        variables.put("id", id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, variables, schemesUrl + "/{id}", HttpMethod.GET, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getBody(), Scheme.class);
    }

    public void deleteScheme(String id) {
        Map<String, String> variables = new HashMap<>();
        variables.put("id", id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, variables, schemesUrl + "/{id}", HttpMethod.DELETE, Object.class);
    }
}
