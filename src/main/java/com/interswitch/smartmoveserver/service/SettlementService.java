package com.interswitch.smartmoveserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.infrastructure.APIRequestClient;
import com.interswitch.smartmoveserver.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * Created by adebola.owolabi on 6/9/2020
 */
@Service
public class SettlementService {
    @Value("${settlement.service.reports-url}")
    private String reportsUrl;
    @Value("${settlement.service.reports-download-url}")
    private String downloadUrl;

    @Autowired
    APIRequestClient apiRequestClient;

    @Autowired
    PassportService passportService;

    public Set<Report> getReports(String username) {
        Map<String, String> variables = new HashMap<>();
        variables.put("name", username);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, variables, reportsUrl + "/{name}", HttpMethod.GET, Object.class);
        return retrieveReports(response.getBody());
    }

    public Resource downloadReport(String fileName, String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(downloadUrl)
                .queryParam("fileName", fileName)
                .queryParam("participant", username);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, null, builder.toUriString(), HttpMethod.GET, Object.class);
        return (Resource) response.getBody();
    }

    private Set<Report> retrieveReports(Object response) {
        ObjectMapper mapper = new ObjectMapper();
        Set<Report> reports = mapper.convertValue(response, new TypeReference<Set<Report>>() {
        });
        return reports;
    }
}
