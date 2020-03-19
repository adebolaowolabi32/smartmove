package com.interswitch.smartmoveserver.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configurable
@Component
public class APIRequestClient {

    private RestTemplate restTemplate;

    @Autowired
    public APIRequestClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    public <S, T> ResponseEntity<T> Process(S request, HttpHeaders headers, Map<String, String> pathVariables, String requestUrl, HttpMethod method, Class<T> responseClass) {
        if(pathVariables == null) pathVariables = new HashMap<>();
        HttpEntity requestEntity = new HttpEntity(request, headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(requestUrl, method, requestEntity, responseClass, pathVariables);
        return responseEntity;
    }
}