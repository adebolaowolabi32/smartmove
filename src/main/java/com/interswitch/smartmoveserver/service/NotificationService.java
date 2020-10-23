//package com.interswitch.prepaid.card.service;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import com.interswitch.prepaid.card.config.PassportProperties;
//import com.interswitch.prepaid.card.model.notification.EmailDetails;
//import com.interswitch.prepaid.card.model.notification.SmsDetails;
//import com.interswitch.prepaid.card.model.notification.SmsNotification;
//import com.interswitchng.auth.AuthConfig;
//import com.interswitchng.email.Email;
//import com.interswitchng.email.EmailClient;
//import com.interswitchng.email.EmailException;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//
//import java.util.Base64;
//import java.util.Locale;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//public class NotificationService {
//
//    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
//
//    @Value("${email.client.id}")
//    private String clientId;
//
//    @Value("${email.client.secret}")
//    private String clientSecret;
//
//    @Value("${notification.sms.endpoint}")
//    private String smsNotificationEndpoint;
//
//    @Value("${sms.queue.name}")
//    private String smsQueueName;
//
//    @Value("${email.queue.name}")
//    private String emailQueueName;
//
//    @Value("${email.url}")
//    private String emailUrl;
//
//    @Value("${default.email.sender}")
//    public String defaultEmailSender;
//
//    @Value("${notification.sms.sender}")
//    public String smsSender;
//
//    @Value("${mufasa.resource.uri}")
//    private String resourceUri;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private PassportProperties passportProperties;
//
//    @Autowired
//    private JmsTemplate emailJmsTemplate;
//
//    private TemplateEngine templateEngine;
//
//    @Autowired
//    public NotificationService(TemplateEngine templateEngine) {
//        this.templateEngine = templateEngine;
//
//    }
//
//    /*public String queueEmail(EmailDetails emailDetails) throws JsonProcessingException {
//        //String marshalledData = xmlMarshaller.marshall(emailDetails);
//        String marshalledData = this.xmlMapper.writeValueAsString(emailDetails);
//        emailJmsTemplate.convertAndSend(emailQueueName, marshalledData);
//        logger.debug("Sent " + marshalledData);
//        return marshalledData;
//    }
//
//    public String queueSms(SmsDetails smsDetails) throws JsonProcessingException {
//        //String marshalledData = xmlMarshaller.marshall(smsDetails);
//        String marshalledData = this.xmlMapper.writeValueAsString(smsDetails);
//        logger.info("sms notification details: " + marshalledData);
////        smsJmsTemplate.send(new QueueMessageCreator(marshalledData));
//        emailJmsTemplate.convertAndSend(smsQueueName, marshalledData);
//        logger.debug("Sent " + marshalledData);
//        return marshalledData;
//    }*/
//
//    public void sendMailAsRest(String recipient, String subject, String template, Map<String, Object> params) {
//        try {
//            AuthConfig config = new AuthConfig("POST", emailUrl,
//                    clientId, clientSecret, "", AuthConfig.SHA1);
//
//            EmailClient client = new EmailClient(restTemplate, config);
//            params.put("mufasa", resourceUri);
//            Context context = new Context(Locale.ENGLISH, params);
//            String message = templateEngine.process(template, context);
//            logger.debug("Email message: "+message);
//            Email email = new Email(defaultEmailSender,
//                    recipient, message, subject, MediaType.TEXT_HTML_VALUE);
//
//            client.send(email, emailUrl);
//        } catch (EmailException e) {
//            logger.error("Error sending email", e);
//            throw e;
//        }
//    }
//
//    public boolean sendSMS(String phoneNumber, String message)
//    {
//        boolean status = false;
//        if((phoneNumber != null) && (message != null))
//        {
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//            httpHeaders.add("Timestamp",String.valueOf(System.currentTimeMillis()));
//            httpHeaders.add("Nonce",UUID.randomUUID().toString().replaceAll("-", ""));
//            httpHeaders.add("Authorization","InterswitchAuth " + Base64.getEncoder().encodeToString(passportProperties.getClientId().getBytes()));
//            httpHeaders.add("SignatureMethod","SHA1");
//
//            StringBuilder stringBuilder = new StringBuilder(HttpMethod.POST.toString())
//                    .append("&").append(this.smsNotificationEndpoint).append("&").append(httpHeaders.get("TimeStamp"))
//                    .append("&").append(httpHeaders.get("Nonce")).append("&").append(passportProperties.getClientId())
//                    .append("&").append(passportProperties.getClientSecret());
//
//            httpHeaders.add("Signature",Base64.getEncoder().encodeToString(DigestUtils.sha1Hex(stringBuilder.toString()).getBytes()));
//
//            SmsNotification smsNotification = new SmsNotification(phoneNumber,message);
//            smsNotification.setSourceAddress(this.smsSender);
//            HttpEntity<SmsNotification> httpEntity = new HttpEntity<>(smsNotification,httpHeaders);
//            ResponseEntity<Integer> responseEntity = this.restTemplate.exchange(this.smsNotificationEndpoint,HttpMethod.POST,httpEntity,Integer.class);
//            status = responseEntity.getStatusCode().is2xxSuccessful();
//        }
//        else
//        {
//            if(phoneNumber == null)
//            {
//                logger.error("Phone Number is empty");
//            }
//
//            if(message == null)
//            {
//                logger.error("Message is empty");
//            }
//        }
//        return status;
//    }
//
//}
