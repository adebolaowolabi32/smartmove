package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.infrastructure.APIRequestClient;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.IswRole;
import com.interswitch.smartmoveserver.model.request.IswUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class IswCoreService {


    @Autowired
    APIRequestClient apiRequestClient;

    @Autowired
    PassportService passportService;

    @Value("${spring.user.url}")
    private String userUrl;
    @Value("${spring.accesstoken}")
    private  String accessToken;
    @Value("${spring.domainCode}")
    private  String domainCode;
    @Value("${spring.appCode}")
    private String appCode ;
    @Value("${spring.user.roles}")
    private String roleurl ;
    @Value("${spring.domainId}")
    private  int domainId;
    @Value("${spring.appId}")
    private int appId ;

    IswRole iswRole;
    IswUser iswUser;


    public IswUser createUser(User user)  {
        IswUser iswUser = buildUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        return apiRequestClient.Process(iswUser, headers, null, userUrl, HttpMethod.POST, IswUser.class).getBody();
    }

/*    public List<String> getRolesByUsername(String username){
        apiRequestClient.setAuthHeader(passportService.getAccessToken());
        ResponseEntity<List<IswRole>> domainResponse = restTemplate.exchange(roleurl, HttpMethod.GET, request, new ParameterizedTypeReference<List<IswRole>>() {},username);
        return domainResponse.getBody();
    }*/

    public  List<String> getUserPermissions(String username,String domainCode,String appCode) throws NoSuchFieldException, IllegalAccessException{
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, passportService.getAccessToken());
        String uri = UriComponentsBuilder
                .fromUriString(userUrl)
                .queryParam("domainCode", domainCode)
                .queryParam("appCode", appCode)
                .queryParam("username", username)
                .build()
                .toUriString();
        ResponseEntity response = apiRequestClient.Process(null, headers, null, uri, HttpMethod.GET, Object.class);
        Map<String, Object> resultToJson = (Map<String, Object>) response.getBody();
        ArrayList permissions = (ArrayList) resultToJson.get("data");
        List<String> userPermissions = new ArrayList<>();
        for (Object permission : permissions) {
            Class<?> clazz = permission.getClass();
            Field field = clazz.getField("name");
            String permissionString = field.get(permission).toString();
            userPermissions.add(permissionString);
        }
        return userPermissions;
    }

    public IswUser buildUser(User user){
        iswRole = new IswRole();
        iswRole.setAppId(domainId);
        iswRole.setDomainId(appId);
        iswRole.setName(user.getRole());
        iswUser = new IswUser();
        iswUser.setDomainCode(domainCode);
        iswUser.setAppCode(appCode);
        iswUser.setId(user.getId());
        iswUser.setMobileNo(user.getMobileNo());
        iswUser.setActive(user.isActive());
        iswUser.setAddress(user.getAddress());
        iswUser.setEmail(user.getEmail());
        iswUser.setUsername(user.getUsername());
        iswUser.setLastName(user.getLastName());
        iswUser.setFirstName(user.getFirstName());
        iswUser.setRoles(Collections.singleton(iswRole));
        return iswUser;
    }
}