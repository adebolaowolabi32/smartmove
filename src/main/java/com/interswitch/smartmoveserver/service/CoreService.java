package com.interswitch.smartmoveserver.service;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoreService {

    public List<String> getUserPermissions(){
        List roles = new ArrayList();
        roles.add("ISW_ADMIN");
        roles.add("REGULATOR");
        roles.add("OPERATOR");
        roles.add("VEHICLE_OWNER");
        roles.add("AGENT");



        List permissions = new ArrayList();
        permissions.add("VIEW_CARDS");
        permissions.add("VIEW_AGENTS");
        permissions.add("VIEW_DEVICES");
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

        permissions.add("CREATE_CARD");
        permissions.add("CREATE_AGENT");
        permissions.add("CREATE_DEVICE");
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

        permissions.add("VIEW_CARD_DETAILS");
        permissions.add("VIEW_AGENT_DETAILS");
        permissions.add("VIEW_DEVICE_DETAILS");
        permissions.add("VIEW_TERMINAL_DETAILS");
        permissions.add("VIEW_OPERATOR_DETAILS");
        permissions.add("VIEW_REGULATOR_DETAILS");
        permissions.add("VIEW_VEHICLE_OWNER_DETAILS");
        permissions.add("VIEW_ADMINISTRATOR_DETAILS");
        permissions.add("VIEW_WALLET_DETAILS");
        permissions.add("VIEW_TRANSACTION_DETAILS");
        permissions.add("VIEW_SETTLEMENT_DETAILS");
        permissions.add("VIEW_ROUTE_DETAILS");
        permissions.add("VIEW_VEHICLE_DETAILS");
        permissions.add("VIEW_CONFIGURATION_DETAILS");

        permissions.add("UPDATE_CARD");
        permissions.add("UPDATE_AGENT");
        permissions.add("UPDATE_DEVICE");
        permissions.add("UPDATE_TERMINAL");
        permissions.add("UPDATE_OPERATOR");
        permissions.add("UPDATE_REGULATOR");
        permissions.add("UPDATE_VEHICLE_OWNER");
        permissions.add("UPDATE_ADMINISTRATOR");
        permissions.add("UPDATE_WALLET");
        permissions.add("UPDATE_ROUTE");
        permissions.add("UPDATE_VEHICLE");
        permissions.add("UPDATE_CONFIGURATION");

        permissions.add("DELETE_CARD");
        permissions.add("DELETE_AGENT");
        permissions.add("DELETE_DEVICE");
        permissions.add("DELETE_TERMINAL");
        permissions.add("DELETE_OPERATOR");
        permissions.add("DELETE_REGULATOR");
        permissions.add("DELETE_VEHICLE_OWNER");
        permissions.add("DELETE_ADMINISTRATOR");
        permissions.add("DELETE_WALLET");
        permissions.add("DELETE_ROUTE");
        permissions.add("DELETE_VEHICLE");
        permissions.add("DELETE_CONFIGURATION");

        return permissions;
    }
}
