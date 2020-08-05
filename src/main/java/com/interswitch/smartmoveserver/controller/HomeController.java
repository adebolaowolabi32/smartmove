package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.*;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author adebola.owolabi
 */
@Controller
public class HomeController {

    @Autowired
    private PassportService passportService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SecurityUtil securityUtil;


    @GetMapping(value = {"/", "/index", "/home"})
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        Enum.Role role = user.getRole();
        Long no_admins = 0L;
        Long no_regulators = 0L;
        Long no_operators = 0L;
        Long no_agents = 0L;
        Long no_terminals = 0L;
        Long no_vehicles = 0L;
        Long no_routes = 0L;
        Long no_readers = 0L;
        Long no_validators = 0L;
        Long no_transactions = 0L;
        Long no_settlements = 0L;
        Long no_cards = 0L;
        Long no_topups = 0L;
        Long no_transfers = 0L;
        Long card_balance = 0L;
        Double wallet_balance = 0D;

        if(securityUtil.isOwnedEntity(role)){
            no_regulators = userService.countByRole(principal, user, Enum.Role.REGULATOR);
            no_operators = userService.countByRole(principal, user, Enum.Role.OPERATOR);
            no_agents = userService.countByRole(principal, user, Enum.Role.AGENT);
            no_vehicles = vehicleService.countByOwner(user);
            no_terminals = terminalService.countByOwner(user);
            no_routes = routeService.countByOwner(user);
            no_validators = deviceService.countByTypeAndOwner(Enum.DeviceType.VALIDATOR, user);
            no_readers = deviceService.countByTypeAndOwner(Enum.DeviceType.READER, user);
            no_transactions = transactionService.countAll();
            if(role == Enum.Role.AGENT ) {
                card_balance = cardService.findByOwner(user.getId()).getBalance();
                wallet_balance = walletService.findByOwner(user).getBalance();
                no_transfers = walletService.countTransfers(principal, user);
            }
        }
        else {
            no_admins = userService.countByRole(principal, null, Enum.Role.ISW_ADMIN);
            no_regulators = userService.countByRole(principal, null, Enum.Role.REGULATOR);
            no_operators = userService.countByRole(principal, null, Enum.Role.OPERATOR);
            no_agents = userService.countByRole(principal, null, Enum.Role.AGENT);
            no_vehicles = vehicleService.countAll();
            no_terminals = terminalService.countAll();
            no_routes = routeService.countAll();
            no_validators = deviceService.countByType(Enum.DeviceType.VALIDATOR);
            no_readers = deviceService.countByType(Enum.DeviceType.READER);
            no_transactions = transactionService.countAll();
            no_cards = cardService.countAll();
        }
        model.addAttribute("no_admins", no_admins);
        model.addAttribute("no_regulators", no_regulators);
        model.addAttribute("no_operators", no_operators);
        model.addAttribute("no_agents", no_agents);
        model.addAttribute("no_vehicles", no_vehicles);
        model.addAttribute("no_terminals", no_terminals);
        model.addAttribute("no_routes", no_routes);
        model.addAttribute("no_validators", no_validators);
        model.addAttribute("no_readers", no_readers);
        model.addAttribute("no_transactions", no_transactions);
        model.addAttribute("no_settlements", 0);
        model.addAttribute("no_cards", no_cards);
        model.addAttribute("card_balance", card_balance);
        model.addAttribute("wallet_balance", wallet_balance);
        model.addAttribute("no_transfers", no_transfers);
        model.addAttribute("no_topups", no_topups);

        DateFormat format = new SimpleDateFormat("MMM dd yyyy HH:mm aa");
        Date dateobj = new Date();
        model.addAttribute("time_date", format.format(dateobj));
        return "dashboard";
    }

}
