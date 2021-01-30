package com.interswitch.smartmoveserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.interswitch.dtos.ApiMessageType;
import com.interswitch.dtos.EmailPayload;
import com.interswitch.dtos.MessageApi;
import com.interswitch.postman.PostmanService;
import com.interswitch.postman.payload.PostmanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class MessagingService {

    private final String SENDER_EMAIL = "support@interswitchgroup.com";

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${isw.post.office.appId}")
    private String appIdOnPostOffice;

    @Autowired
    private PassportService passportService;

    @Autowired
    private PostmanService postmanService;

    private ObjectMapper objectMapper;

    public MessagingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Async
    public void sendEmail(String recipient, String subject, String template, Map<String, Object> params) {
        try {
            Context context = new Context(Locale.ENGLISH, params);
            String message = templateEngine.process(template, context);
            log.info("EmailMessage==>" + message);
            EmailPayload emailPayload = new EmailPayload();
            emailPayload.setContentType(MediaType.TEXT_PLAIN_VALUE);
            emailPayload.setMessage(message);
            emailPayload.setRecipient(recipient);
            emailPayload.setSender(SENDER_EMAIL);
            emailPayload.setSubject(subject);
            String payload = objectMapper.writeValueAsString(emailPayload);
            MessageApi messageApi = new MessageApi();
            messageApi.setType(ApiMessageType.EMAIL);
            messageApi.setAccessToken(passportService.getAccessToken());
            messageApi.setAppId(appIdOnPostOffice);
            messageApi.setUuid(UUID.randomUUID().toString());
            messageApi.setPayload(payload);
            postmanService.postData(messageApi);
        } catch (PostmanException | JsonProcessingException ex) {
            log.error("An error happened while trying to send email with isw-post-office==>" + ex.getLocalizedMessage());
        }
    }
}