//package com.interswitch.smartmoveserver.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.BufferingClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.Authenticator;
//import java.net.InetSocketAddress;
//import java.net.PasswordAuthentication;
//import java.net.Proxy;
//import java.util.Collections;
//
///**
// * @author adebola.owolabi
// */
//@Configuration
//public class RestTemplateConfig {
//
//
///*    @Autowired
//    HttpComponentsClientHttpRequestFactory httpRequestFactory;
//
//    @Autowired
//    HttpClient httpClient;
//
// @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
//        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
//        for (HttpMessageConverter<?> converter : converters) {
//            if (converter instanceof MappingJackson2HttpMessageConverter) {
//                MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
//                jsonConverter.setObjectMapper(new ObjectMapper());
//                jsonConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET), new MediaType("text", "javascript", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET)));
//            }
//        }
//        return restTemplate;
//    }
//
//    public HttpComponentsClientHttpRequestFactory httpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        clientHttpRequestFactory.setHttpClient(httpClient);
//        return clientHttpRequestFactory;
//    }*/
//
//
//
//    @Value("${request.connect-timeout}")
//    private int connectTimeout;
//    @Value("${request.read-timeout}")
//    private int readTimeout;
//    @Value("${request.proxy.host}")
//    private String proxyIp;
//    @Value("${request.proxy.port}")
//    private int proxyPort;
//    @Value("${request.proxy.user}")
//    private String proxyUser;
//    @Value("${request.proxy.password}")
//    private String proxyPassword;
//    @Value("${request.proxy.protocol}")
//    String proxyProtocol;
//    @Value("${request.proxy.enabled}")
//    boolean proxyEnabled;
//
//    @Bean("ProxyRestTemplate")
//    public RestTemplate proxyRestTemplate() {
//
//        if(proxyEnabled){
//            Authenticator authenticator = new Authenticator() {
//
//                public PasswordAuthentication getPasswordAuthentication() {
//                    return (new PasswordAuthentication(proxyUser,
//                            proxyPassword.toCharArray()));
//                }
//            };
//            Authenticator.setDefault(authenticator);
//
//            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//            Proxy proxy = new Proxy(Proxy.Type.valueOf(proxyProtocol.toUpperCase()), new InetSocketAddress(proxyIp, proxyPort));
//            requestFactory.setProxy(proxy);
//            requestFactory.setReadTimeout(readTimeout);
//            requestFactory.setConnectTimeout(connectTimeout);
//            RestTemplate template = basicRestTemplate();
//            template.setRequestFactory(new BufferingClientHttpRequestFactory(requestFactory));
//            return template;
//        }
//        else{
//            return basicRestTemplate();
//        }
//    }
//    @Bean("BasicRestTemplate")
//    public RestTemplate basicRestTemplate() {
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setReadTimeout(readTimeout);
//        factory.setConnectTimeout(connectTimeout);
//        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(factory));
//        restTemplate.setRequestFactory(factory);
//        return restTemplate;
//    }
//
//}
