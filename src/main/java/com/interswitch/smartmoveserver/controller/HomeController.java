package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.*;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@Controller
public class HomeController {

    @Autowired
    private PassportService passportService;

    @Autowired
    private CoreService coreService;

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
    @Layout(Layout.NONE)
    public String home(Model model) {
        return "index";
    }

    @Layout(value = "layouts/default")
    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        User user = userService.findOrCreateUser(principal);
        Enum.Role role = user.getRole();
        if(securityUtil.isOwnedEntity(role)){
            model.addAttribute("no_admins", userService.countByRoleAndOwner(user, Enum.Role.ISW_ADMIN));
            model.addAttribute("no_regulators", userService.countByRoleAndOwner(user, Enum.Role.REGULATOR));
            model.addAttribute("no_operators", userService.countByRoleAndOwner(user, Enum.Role.OPERATOR));
            model.addAttribute("no_agents", userService.countByRoleAndOwner(user, Enum.Role.AGENT));
            model.addAttribute("no_vehicles", vehicleService.countByOwner(user));
            model.addAttribute("no_terminals", terminalService.countByOwner(user));
            model.addAttribute("no_routes", routeService.countByOwner(user));
            model.addAttribute("no_devices", deviceService.countByOwner(user));
            model.addAttribute("no_transactions", transactionService.countAll());
            model.addAttribute("no_settlements", 0);
            model.addAttribute("no_cards", cardService.countByOwner(user));
            if(role == Enum.Role.AGENT ) {
                model.addAttribute("wallet_balance", walletService.findByOwner(user).getBalance());
            }
        }
        else {
            model.addAttribute("no_admins", userService.countByRole(Enum.Role.ISW_ADMIN));
            model.addAttribute("no_regulators", userService.countByRole(Enum.Role.REGULATOR));
            model.addAttribute("no_operators", userService.countByRole(Enum.Role.OPERATOR));
            model.addAttribute("no_agents", userService.countByRole(Enum.Role.AGENT));
            model.addAttribute("no_vehicles", vehicleService.countAll());
            model.addAttribute("no_terminals", terminalService.countAll());
            model.addAttribute("no_routes", routeService.countAll());
            model.addAttribute("no_devices", deviceService.countAll());
            model.addAttribute("no_transactions", transactionService.countAll());
            model.addAttribute("no_settlements", 0);
            model.addAttribute("no_cards", cardService.countAll());
        }

        return "dashboard";
    }

}
