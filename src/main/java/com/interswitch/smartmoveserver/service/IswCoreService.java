package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.infrastructure.APIRequestClient;
import com.interswitch.smartmoveserver.model.Enum;
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

    @Value("${spring.application.core.user-url}")
    private String userUrl;
    @Value("${spring.application.core.user-permission-url}")
    private  String accessToken;
    @Value("${spring.application.domainCode}")
    private  String domainCode;
    @Value("${spring.application.appCode}")
    private String appCode ;
    @Value("${spring.application.domainId}")
    private  int domainId;
    @Value("${spring.application.appId}")
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

    public  List<String> getPermissions(User user) {
        List<String> permissions = new ArrayList<>();
        Enum.Role role = user.getRole();
        switch (role) {
            case ISW_ADMIN: {
                permissions = new ArrayList<>();
                permissions.add("VIEW_CARDS");
                permissions.add("VIEW_AGENTS");
                permissions.add("VIEW_DEVICES");
                permissions.add("VIEW_READERS");
                permissions.add("VIEW_VALIDATORS");
                permissions.add("VIEW_TERMINALS");
                permissions.add("VIEW_OPERATORS");
                permissions.add("VIEW_REGULATORS");
                permissions.add("VIEW_VEHICLE_OWNERS");
                permissions.add("VIEW_ADMINISTRATORS");
                permissions.add("VIEW_TRANSACTIONS");
                permissions.add("VIEW_SETTLEMENTS");
                permissions.add("VIEW_ROUTES");
                permissions.add("VIEW_VEHICLES");
                permissions.add("VIEW_CONFIGURATIONS");
                permissions.add("VIEW_BLACKLISTS");
                permissions.add("VIEW_TRIPS");

                permissions.add("CREATE_CARD");
                permissions.add("CREATE_AGENT");
                permissions.add("CREATE_DEVICE");
                permissions.add("CREATE_READER");
                permissions.add("CREATE_VALIDATOR");
                permissions.add("CREATE_TERMINAL");
                permissions.add("CREATE_OPERATOR");
                permissions.add("CREATE_REGULATOR");
                permissions.add("CREATE_VEHICLE_OWNER");
                permissions.add("CREATE_ADMINISTRATOR");
                permissions.add("CREATE_WALLET");
                permissions.add("CREATE_TRANSACTION");
                permissions.add("CREATE_SETTLEMENT");
                permissions.add("CREATE_ROUTE");
                permissions.add("CREATE_VEHICLE");
                permissions.add("CREATE_CONFIGURATION");
                permissions.add("CREATE_BLACKLIST");
                permissions.add("CREATE_TRIP");


                permissions.add("VIEW_AGENT_DETAILS");
                permissions.add("VIEW_DEVICE_DETAILS");
                permissions.add("VIEW_READER_DETAILS");
                permissions.add("VIEW_VALIDATOR_DETAILS");
                permissions.add("VIEW_TERMINAL_DETAILS");
                permissions.add("VIEW_OPERATOR_DETAILS");
                permissions.add("VIEW_REGULATOR_DETAILS");
                permissions.add("VIEW_VEHICLE_OWNER_DETAILS");
                permissions.add("VIEW_ADMINISTRATOR_DETAILS");
                permissions.add("VIEW_TRANSACTION_DETAILS");
                permissions.add("VIEW_SETTLEMENT_DETAILS");
                permissions.add("VIEW_ROUTE_DETAILS");
                permissions.add("VIEW_CONFIGURATION_DETAILS");
                permissions.add("VIEW_BLACKLIST_DETAILS");
                permissions.add("CREATE_TRIP_DETAILS");


                permissions.add("UPDATE_CARD");
                permissions.add("UPDATE_AGENT");
                permissions.add("UPDATE_DEVICE");
                permissions.add("UPDATE_READER");
                permissions.add("UPDATE_VALIDATOR");
                permissions.add("UPDATE_TERMINAL");
                permissions.add("UPDATE_OPERATOR");
                permissions.add("UPDATE_REGULATOR");
                permissions.add("UPDATE_VEHICLE_OWNER");
                permissions.add("UPDATE_ADMINISTRATOR");
                permissions.add("UPDATE_WALLET");
                permissions.add("UPDATE_ROUTE");
                permissions.add("UPDATE_VEHICLE");
                permissions.add("UPDATE_CONFIGURATION");
                permissions.add("UPDATE_TRIP");

                permissions.add("DELETE_CARD");
                permissions.add("DELETE_AGENT");
                permissions.add("DELETE_DEVICE");
                permissions.add("DELETE_READER");
                permissions.add("DELETE_VALIDATOR");
                permissions.add("DELETE_TERMINAL");
                permissions.add("DELETE_OPERATOR");
                permissions.add("DELETE_REGULATOR");
                permissions.add("DELETE_VEHICLE_OWNER");
                permissions.add("DELETE_ADMINISTRATOR");
                permissions.add("DELETE_WALLET");
                permissions.add("DELETE_ROUTE");
                permissions.add("DELETE_VEHICLE");
                permissions.add("DELETE_CONFIGURATION");
                permissions.add("DELETE_BLACKLIST");
                permissions.add("DELETE_TRIP");

                return permissions;
            }
            case REGULATOR: {
                permissions = new ArrayList<>();
                permissions.add("VIEW_AGENTS");
                permissions.add("VIEW_DEVICES");
                permissions.add("VIEW_READERS");
                permissions.add("VIEW_VALIDATORS");
                permissions.add("VIEW_TERMINALS");
                permissions.add("VIEW_OPERATORS");
                permissions.add("VIEW_REGULATORS");
                permissions.add("VIEW_VEHICLE_OWNERS");
                permissions.add("VIEW_TRANSACTIONS");
                permissions.add("VIEW_SETTLEMENTS");
                permissions.add("VIEW_ROUTES");
                permissions.add("VIEW_VEHICLES");
                permissions.add("VIEW_TRIPS");

                permissions.add("CREATE_AGENT");
                permissions.add("CREATE_DEVICE");
                permissions.add("CREATE_READER");
                permissions.add("CREATE_VALIDATOR");
                permissions.add("CREATE_TERMINAL");
                permissions.add("CREATE_OPERATOR");
                permissions.add("CREATE_REGULATOR");
                permissions.add("CREATE_VEHICLE_OWNER");
                permissions.add("CREATE_ROUTE");
                permissions.add("CREATE_VEHICLE");
                permissions.add("CREATE_TRIP");

                permissions.add("VIEW_AGENT_DETAILS");
                permissions.add("VIEW_DEVICE_DETAILS");
                permissions.add("VIEW_READER_DETAILS");
                permissions.add("VIEW_VALIDATOR_DETAILS");
                permissions.add("VIEW_TERMINAL_DETAILS");
                permissions.add("VIEW_OPERATOR_DETAILS");
                permissions.add("VIEW_REGULATOR_DETAILS");
                permissions.add("VIEW_VEHICLE_OWNER_DETAILS");
                permissions.add("VIEW_TRANSACTION_DETAILS");
                permissions.add("VIEW_SETTLEMENT_DETAILS");
                permissions.add("VIEW_ROUTE_DETAILS");
                permissions.add("VIEW_VEHICLE_DETAILS");
                permissions.add("VIEW_TRIP_DETAILS");

                permissions.add("UPDATE_AGENT");
                permissions.add("UPDATE_DEVICE");
                permissions.add("UPDATE_READER");
                permissions.add("UPDATE_VALIDATOR");
                permissions.add("UPDATE_TERMINAL");
                permissions.add("UPDATE_OPERATOR");
                permissions.add("UPDATE_REGULATOR");
                permissions.add("UPDATE_VEHICLE_OWNER");
                permissions.add("UPDATE_ROUTE");
                permissions.add("UPDATE_VEHICLE");
                permissions.add("UPDATE_TRIP");

                permissions.add("DELETE_AGENT");
                permissions.add("DELETE_DEVICE");
                permissions.add("DELETE_READER");
                permissions.add("DELETE_VALIDATOR");
                permissions.add("DELETE_TERMINAL");
                permissions.add("DELETE_OPERATOR");
                permissions.add("DELETE_REGULATOR");
                permissions.add("DELETE_VEHICLE_OWNER");
                permissions.add("DELETE_ROUTE");
                permissions.add("DELETE_VEHICLE");
                permissions.add("DELETE_TRIP");
                return permissions;
            }
            case OPERATOR: {
                permissions = new ArrayList<>();
                permissions.add("VIEW_AGENTS");
                permissions.add("VIEW_DEVICES");
                permissions.add("VIEW_READERS");
                permissions.add("VIEW_VALIDATORS");
                permissions.add("VIEW_TERMINALS");
                permissions.add("VIEW_REGULATORS");
                permissions.add("VIEW_OPERATORS");
                permissions.add("VIEW_VEHICLE_OWNERS");
                permissions.add("VIEW_TRANSACTIONS");
                permissions.add("VIEW_SETTLEMENTS");
                permissions.add("VIEW_ROUTES");
                permissions.add("VIEW_VEHICLES");
                permissions.add("VIEW_TRIPS");

                permissions.add("CREATE_AGENT");
                permissions.add("CREATE_DEVICE");
                permissions.add("CREATE_READER");
                permissions.add("CREATE_VALIDATOR");
                permissions.add("CREATE_TERMINAL");
                permissions.add("CREATE_OPERATOR");
                permissions.add("CREATE_VEHICLE_OWNER");
                permissions.add("CREATE_ROUTE");
                permissions.add("CREATE_VEHICLE");
                permissions.add("CREATE_TRIP");

                permissions.add("VIEW_AGENT_DETAILS");
                permissions.add("VIEW_DEVICE_DETAILS");
                permissions.add("VIEW_READER_DETAILS");
                permissions.add("VIEW_VALIDATOR_DETAILS");
                permissions.add("VIEW_TERMINAL_DETAILS");
                permissions.add("VIEW_REGULATOR_DETAILS");
                permissions.add("VIEW_OPERATOR_DETAILS");
                permissions.add("VIEW_VEHICLE_OWNER_DETAILS");
                permissions.add("VIEW_TRANSACTION_DETAILS");
                permissions.add("VIEW_SETTLEMENT_DETAILS");
                permissions.add("VIEW_ROUTE_DETAILS");
                permissions.add("VIEW_VEHICLE_DETAILS");
                permissions.add("VIEW_TRIP_DETAILS");

                permissions.add("UPDATE_AGENT");
                permissions.add("UPDATE_DEVICE");
                permissions.add("UPDATE_READER");
                permissions.add("UPDATE_VALIDATOR");
                permissions.add("UPDATE_TERMINAL");
                permissions.add("UPDATE_OPERATOR");
                permissions.add("UPDATE_VEHICLE_OWNER");
                permissions.add("UPDATE_ROUTE");
                permissions.add("UPDATE_VEHICLE");
                permissions.add("UPDATE_TRIP");

                permissions.add("DELETE_AGENT");
                permissions.add("DELETE_DEVICE");
                permissions.add("DELETE_READER");
                permissions.add("DELETE_VALIDATOR");
                permissions.add("DELETE_TERMINAL");
                permissions.add("DELETE_OPERATOR");
                permissions.add("DELETE_VEHICLE_OWNER");
                permissions.add("DELETE_ROUTE");
                permissions.add("DELETE_VEHICLE");
                permissions.add("DELETE_TRIP");
                return permissions;
            }
            case VEHICLE_OWNER: {
                permissions = new ArrayList<>();
                permissions.add("VIEW_TERMINALS");
                permissions.add("VIEW_OPERATORS");
                permissions.add("VIEW_SETTLEMENTS");
                permissions.add("VIEW_ROUTES");
                permissions.add("VIEW_VEHICLES");
                permissions.add("VIEW_TRIPS");

                permissions.add("VIEW_TERMINAL_DETAILS");
                permissions.add("VIEW_OPERATOR_DETAILS");
                permissions.add("VIEW_SETTLEMENT_DETAILS");
                permissions.add("VIEW_ROUTE_DETAILS");
                permissions.add("VIEW_VEHICLE_DETAILS");
                permissions.add("VIEW_TRIP_DETAILS");
                return permissions;
            }
            case AGENT: {
                permissions = new ArrayList<>();
                permissions.add("VIEW_READERS");
                permissions.add("VIEW_TERMINALS");
                permissions.add("VIEW_REGULATORS");
                permissions.add("VIEW_TRANSACTIONS");


                permissions.add("CREATE_READER");

                permissions.add("VIEW_CARD_DETAILS");
                permissions.add("VIEW_READER_DETAILS");
                permissions.add("VIEW_TERMINAL_DETAILS");
                permissions.add("VIEW_REGULATOR_DETAILS");
                permissions.add("VIEW_WALLET_DETAILS");
                permissions.add("VIEW_TRANSACTION_DETAILS");

                permissions.add("UPDATE_READER");
                permissions.add("UPDATE_WALLET");
                return permissions;
            }
           default:
                return permissions;
        }

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
        iswUser.setEnabled(user.isEnabled());
        iswUser.setEmail(user.getEmail());
        iswUser.setUsername(user.getUsername());
        iswUser.setLastName(user.getLastName());
        iswUser.setFirstName(user.getFirstName());
        iswUser.setRoles(Collections.singleton(iswRole));
        return iswUser;
    }
}