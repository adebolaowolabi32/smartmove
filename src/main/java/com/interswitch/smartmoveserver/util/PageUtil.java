package com.interswitch.smartmoveserver.util;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.PageView;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author adebola.owolabi
 */
@Component
public class PageUtil {
    private static List<Enum.Role> ownersForAgents = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.OPERATOR, Enum.Role.AGENT, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForOperators = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.OPERATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForRegulators = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForVehicleOwners = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.OPERATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForDrivers = Arrays.asList(Enum.Role.OPERATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForTicketers = Arrays.asList(Enum.Role.OPERATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForServiceProviders = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForInspectors = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.OPERATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForCards = Arrays.asList(Enum.Role.AGENT, Enum.Role.DRIVER, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForDevices = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.OPERATOR, Enum.Role.AGENT, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForRoutes = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.OPERATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForSchedules = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.OPERATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForTerminals = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.OPERATOR, Enum.Role.ISW_ADMIN);
    private static List<Enum.Role> ownersForVehicles = Arrays.asList(Enum.Role.REGULATOR, Enum.Role.OPERATOR, Enum.Role.VEHICLE_OWNER, Enum.Role.ISW_ADMIN);

    public <T> List<Long> getPageNumber(PageView<T> page) {
        List<Long> pageNumbers = new ArrayList<>();
        long pageCount = page.getCount();
        if (pageCount > 0) {
            pageNumbers = LongStream.rangeClosed(1, pageCount).boxed().collect(Collectors.toList());
        }
        return pageNumbers;
    }

    public PageRequest buildPageRequest(int page, int size){
        if(page == 0) page = 1;
        if(size == 0) size = 10;
        return PageRequest.of(page - 1, size);
    }

    public String buildTitle(Enum.Role role){
        String title = "";
        switch(role) {
            case ISW_ADMIN:
                title = "Administrator";
                break;
            case EXECUTIVE:
                title = "Executive";
                break;
            case REGULATOR:
                title = "Regulator";
                break;
            case OPERATOR:
                title = "Operator";
                break;
            case VEHICLE_OWNER:
                title = "Vehicle Owner";
                break;
            case AGENT:
                title = "Agent";
                break;
            case DRIVER:
                title = "Driver";
                break;
            case SERVICE_PROVIDER:
                title = "Service Provider";
                break;
            case INSPECTOR:
                title = "Inspector";
                break;
            case TICKETER:
                title = "Ticketer";
                break;
            case COMMUTER:
                title = "Commuter";
                break;
            default:
                title = "No Title";
                break;
        }
        return title;
    }

    public String buildSaveMessage(Enum.Role role){
        String message = "";
        switch(role) {
            case ISW_ADMIN:
                message = "administrator.saved.message";
                break;
            case EXECUTIVE:
                message = "executive.saved.message";
                break;
            case REGULATOR:
                message = "regulator.saved.message";
                break;
            case OPERATOR:
                message = "operator.saved.message";
                break;
            case VEHICLE_OWNER:
                message = "vehicle-owner.saved.message";
                break;
            case AGENT:
                message = "agent.saved.message";
                break;
            case SERVICE_PROVIDER:
                message = "service-provider.saved.message";
                break;
            case INSPECTOR:
                message = "inspector.saved.message";
                break;
            case TICKETER:
                message = "ticketer.saved.message";
                break;
            case DRIVER:
                message = "driver.saved.message";
                break;
            default:
                message = "No Message";
                break;
        }
        return message;
    }

    public String buildUpdateMessage(Enum.Role role){
        String message = "";
        switch(role) {
            case ISW_ADMIN:
                message = "administrator.updated.message";
                break;
            case EXECUTIVE:
                message = "executive.updated.message";
                break;
            case REGULATOR:
                message = "regulator.updated.message";
                break;
            case OPERATOR:
                message = "operator.updated.message";
                break;
            case VEHICLE_OWNER:
                message = "vehicle-owner.updated.message";
                break;
            case AGENT:
                message = "agent.updated.message";
                break;
            case SERVICE_PROVIDER:
                message = "service-provider.updated.message";
                break;
            case INSPECTOR:
                message = "inspector.updated.message";
                break;
            case TICKETER:
                message = "ticketer.updated.message";
                break;
            case DRIVER:
                message = "driver.updated.message";
                break;
            default:
                message = "No Message";
                break;
        }
        return message;
    }

    public String buildDeleteMessage(Enum.Role role){
        String message = "";
        switch(role) {
            case ISW_ADMIN:
                message = "administrator.deleted.message";
                break;
            case EXECUTIVE:
                message = "executive.deleted.message";
                break;
            case REGULATOR:
                message = "regulator.deleted.message";
                break;
            case OPERATOR:
                message = "operator.deleted.message";
                break;
            case VEHICLE_OWNER:
                message = "vehicle-owner.deleted.message";
                break;
            case AGENT:
                message = "agent.deleted.message";
                break;
            case SERVICE_PROVIDER:
                message = "service-provider.deleted.message";
                break;
            case INSPECTOR:
                message = "inspector.deleted.message";
                break;
            case TICKETER:
                message = "ticketer.deleted.message";
                break;
            case DRIVER:
                message = "driver.deleted.message";
                break;
            default:
                message = "No Message";
                break;
        }
        return message;
    }

    public List<Enum.Role> getOwners(Enum.Role role) {
        List<Enum.Role> roles = new ArrayList<>();
        switch (role) {
            case REGULATOR:
                roles = ownersForRegulators;
                break;
            case OPERATOR:
                roles = ownersForOperators;
                break;
            case VEHICLE_OWNER:
                roles = ownersForVehicleOwners;
                break;
            case AGENT:
                roles = ownersForAgents;
                break;
            case DRIVER:
                roles = ownersForDrivers;
                break;
            case SERVICE_PROVIDER:
                roles = ownersForServiceProviders;
                break;
            case INSPECTOR:
                roles = ownersForInspectors;
                break;
            case TICKETER:
                roles = ownersForTicketers;
                break;
            default:
                break;
        }
        return roles;
    }

    public List<Enum.Role> getOwners(String entity) {
        List<Enum.Role> roles = new ArrayList<>();
        switch (entity) {
            case "card":
                roles = ownersForCards;
                break;
            case "device":
                roles = ownersForDevices;
                break;
            case "route":
                roles = ownersForRoutes;
                break;
            case "schedule":
                roles = ownersForSchedules;
                break;
            case "terminal":
                roles = ownersForTerminals;
                break;
            case "vehicle":
                roles = ownersForVehicles;
                break;
            default:
                break;
        }
        return roles;
    }

    public List<Enum.Role> getRoles() {
        return Stream.of(Enum.Role.values())
                .collect(Collectors.toList());
    }


}