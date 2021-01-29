package com.interswitch.smartmoveserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.infrastructure.APIRequestClient;
import com.interswitch.smartmoveserver.model.Participant;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * Created by adebola.owolabi on 6/18/2020
 */
@Service
public class ParticipantService {
    @Value("${settlement.service.participants-url}")
    private String participantsUrl;

    @Autowired
    APIRequestClient apiRequestClient;

    @Autowired
    PassportService passportService;

    public Set<Participant> getParticipants() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, null, participantsUrl, HttpMethod.GET, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getBody(), new TypeReference<Set<Participant>>() {
        });
    }

    public Set<Participant> getParticipantsForScheme(String schemeId) {
        String uri = UriComponentsBuilder.fromUriString(participantsUrl + "/byscheme")
                .queryParam("schemeId", schemeId)
                .build()
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, null, uri, HttpMethod.GET, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getBody(), new TypeReference<Set<Participant>>() {
        });
    }


    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Participant createParticipant(Participant participant) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(participant, headers, null, participantsUrl, HttpMethod.POST, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getBody(), Participant.class);
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Participant updateParticipant(Participant participant) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(participant, headers, null, participantsUrl, HttpMethod.PUT, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getBody(), Participant.class);
    }

    public Participant findParticipant(String id) {
        Map<String, String> variables = new HashMap<>();
        variables.put("id", id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, variables, participantsUrl + "/{id}", HttpMethod.GET, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getBody(), Participant.class);
    }

    public void deleteParticipant(String id) {
        Map<String, String> variables = new HashMap<>();
        variables.put("id", id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        ResponseEntity response = apiRequestClient.Process(null, headers, variables, participantsUrl + "/{id}", HttpMethod.DELETE, Object.class);
    }
}
