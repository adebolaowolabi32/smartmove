package com.interswitch.smartmoveserver.service;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
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
        Set<Report> reports = new HashSet<>();
        Report report = new Report();
        report.setFileName("Settlements " + LocalDate.now().minusDays(22));
        report.setSchemeName("Transco");
        report.setParticipantName("AKZ Travels");
        report.setCreatedDate(LocalDateTime.now().minusDays(22));
        reports.add(report);
        Report report1 = new Report();
        report1.setFileName("Settlements " + LocalDate.now().minusDays(21));
        report1.setSchemeName("Transco");
        report1.setParticipantName("AKZ Travels");
        report1.setCreatedDate(LocalDateTime.now().minusDays(21));
        reports.add(report1);
        Report report2 = new Report();
        report2.setFileName("Settlements " + LocalDate.now().minusDays(20));
        report2.setSchemeName("Transco");
        report2.setParticipantName("AKZ Travels");
        report2.setCreatedDate(LocalDateTime.now().minusDays(20));
        reports.add(report2);
        Report report3 = new Report();
        report3.setFileName("Settlements " + LocalDate.now().minusDays(19));
        report3.setSchemeName("Transco");
        report3.setParticipantName("AKZ Travels");
        report3.setCreatedDate(LocalDateTime.now().minusDays(19));
        reports.add(report3);
        Report report4 = new Report();
        report4.setFileName("Settlements " + LocalDate.now().minusDays(18));
        report4.setSchemeName("Transco");
        report4.setParticipantName("AKZ Travels");
        report4.setCreatedDate(LocalDateTime.now().minusDays(18));
        reports.add(report4);
        return reports;
    }

/*    public Set<Report> getReports(String username) {
        Map<String, String> variables = new HashMap<>();
        variables.put("name", username);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, variables, reportsUrl + "/{name}", HttpMethod.GET, Object.class);
        return retrieveReports(response.getBody());
    }



    private Set<Report> retrieveReports(Object response) {
        ObjectMapper mapper = new ObjectMapper();
        Set<Report> reports = mapper.convertValue(response, new TypeReference<Set<Report>>() {
        });
        return reports;
    }*/

    public Resource downloadReport(String fileName, String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(downloadUrl)
                .queryParam("fileName", fileName)
                .queryParam("participant", username);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, null, builder.toUriString(), HttpMethod.GET, Object.class);
        return (Resource) response.getBody();
    }
}
