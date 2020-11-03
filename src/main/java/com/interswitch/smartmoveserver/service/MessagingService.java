package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.infrastructure.APIRequestClient;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.notification.SmsNotification;
import com.interswitch.smartmoveserver.model.request.PassportUser;
import com.interswitchng.auth.AuthConfig;
import com.interswitchng.email.Email;
import com.interswitchng.email.EmailClient;
import com.interswitchng.email.EmailException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

    @Value("${notification.email.client.id}")
    private String emailClientId;

    @Value("${notification.email.client.secret}")
    private String emailClientSecret;

    @Value("${notification.email.queue.name}")
    private String emailQueueName;

    @Value("${notification.email.url}")
    private String emailUrl;

    @Value("${notification.email.defaultSender}")
    public String defaultEmailSender;

    @Value("${notification.sms.endpoint}")
    private String smsNotificationEndpoint;

    @Value("${notification.sms.queue.name}")
    private String smsQueueName;

    @Value("${notification.sms.sender}")
    public String smsSender;

    //passport properties
    @Value("${spring.security.oauth2.client.registration.passport.client-id}")
    private String passportClientId;
    @Value("${spring.security.oauth2.client.registration.passport.client-secret}")
    private String passportClientSecret;


    @Autowired
    APIRequestClient apiRequestClient;

    private TemplateEngine templateEngine;

    @Autowired
    public MessagingService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    public void sendMailAsRest(String recipient, String subject, String template, Map<String, Object> params) {
        try {
            AuthConfig config = new AuthConfig("POST", emailUrl, emailClientId, emailClientSecret, "", AuthConfig.SHA1);
            //EmailClient client = new EmailClient(config);
            Context context = new Context(Locale.ENGLISH, params);
            String message = templateEngine.process(template, context);
            logger.debug("Email message: "+message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", config.getAuthorization());
            logger.info("config auth==>"+config.getAuthorization());
            headers.set("Timestamp", Long.toString(config.getTimestamp()));
            logger.info("config Timestamp==>"+config.getTimestamp());
            headers.set("Nonce", config.getNonce());
            logger.info("config Nonce==>"+config.getNonce());
            headers.set("Signature", config.getSignature());
            logger.info("config Signature==>"+config.getSignature());
            headers.set("SignatureMethod", config.getCrypto().replace("-", ""));
            logger.info("config SignatureMethod==>"+config.getCrypto().replace("-", ""));

            logger.info("Wanna call API endpoint===>");
            Email email = new Email(defaultEmailSender, recipient, message, subject, MediaType.TEXT_HTML_VALUE);
            ResponseEntity response = apiRequestClient.Process(email, headers, null, emailUrl, HttpMethod.POST, Object.class);
            logger.info("Response from call===>"+response.getStatusCode());

        } catch (EmailException e) {
            logger.error("Error sending email", e);
            throw e;
        }
    }


    @Async
    public boolean sendSMS(String phoneNumber, String message) {
        logger.info("Inside sendSMS");
        boolean status = false;
        System.out.println("Passport Client Id===>"+passportClientId);
        System.out.println("Passport Client Secret===>"+passportClientSecret);

        if ((phoneNumber != null) && (message != null)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.add("Timestamp", String.valueOf(System.currentTimeMillis()));
            httpHeaders.add("Nonce", UUID.randomUUID().toString().replaceAll("-", ""));
            httpHeaders.add("Authorization", "InterswitchAuth " + Base64.getEncoder().encodeToString(passportClientId.getBytes()));
            httpHeaders.add("SignatureMethod", "SHA1");

            StringBuilder stringBuilder = new StringBuilder(HttpMethod.POST.toString())
                    .append("&").append(this.smsNotificationEndpoint).append("&").append(httpHeaders.get("TimeStamp"))
                    .append("&").append(httpHeaders.get("Nonce")).append("&").append(passportClientId)
                    .append("&").append(passportClientSecret);

            httpHeaders.add("Signature", Base64.getEncoder().encodeToString(DigestUtils.sha1Hex(stringBuilder.toString()).getBytes()));

            SmsNotification smsNotification = new SmsNotification(phoneNumber, message);
            smsNotification.setSourceAddress(this.smsSender);
            HttpEntity<SmsNotification> httpEntity = new HttpEntity<>(smsNotification, httpHeaders);
            logger.info("Wanna do sendSMS to remote API");

            ResponseEntity<Integer> responseEntity = null;
            try {
               // responseEntity = this.restTemplate.exchange(this.smsNotificationEndpoint, HttpMethod.POST, httpEntity, Integer.class);
                status = responseEntity.getStatusCode().is2xxSuccessful();
            } catch (Exception ex) {
                logger.info("" + ex);
            }
        } else {
            if (phoneNumber == null) {
                logger.error("Phone Number is empty");
            }

            if (message == null) {
                logger.error("Message is empty");
            }
        }
        return status;
    }

}
