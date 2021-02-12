package com.interswitch.smartmoveserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.request.ChangePassword;
import com.interswitch.smartmoveserver.model.request.UserLoginRequest;
import com.interswitch.smartmoveserver.model.request.UserRegRequest;
import com.interswitch.smartmoveserver.model.request.UserRegistration;
import com.interswitch.smartmoveserver.service.*;
import com.interswitch.smartmoveserver.util.ErrorResponseUtil;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author adebola.owolabi
 */
@Slf4j
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
    private TransferService transferService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private TripService tripService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    IswCoreService coreService;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    ErrorResponseUtil errorResponseUtil;

    @GetMapping(value = {"/", "/dashboard"})
    public String dashboard(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        Enum.Role role = user.getRole();
        Wallet wallet = null;
        Card card = null;

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
        Long no_settlements = 5L; //replace with real data
        Long no_cards = 0L;
        Long no_topups = 0L;
        Long no_transfers = 0L;
        Long card_balance = 0L;
        Double wallet_balance = 0D;
        Long no_trips = 0L;
        Long no_service_providers = 0L;
        Long no_inspectors = 0L;
        Long no_ticketers = 0L;
        Long no_drivers = 0L;

        String username = principal.getName();
        if(securityUtil.isOwnedEntity(role)){
            no_regulators = userService.countByRole(username, user, Enum.Role.REGULATOR);
            no_operators = userService.countByRole(username, user, Enum.Role.OPERATOR);
            no_agents = userService.countByRole(username, user, Enum.Role.AGENT);
            no_service_providers = userService.countByRole(username, user, Enum.Role.SERVICE_PROVIDER);
            no_inspectors = userService.countByRole(username, user, Enum.Role.INSPECTOR);
            no_ticketers = userService.countByRole(username, user, Enum.Role.TICKETER);
            no_drivers = userService.countByRole(username, user, Enum.Role.DRIVER);
            no_vehicles = vehicleService.countByOwner(user);
            no_terminals = terminalService.countByOwner(user);
            no_routes = routeService.countByOwner(user);
            no_validators = deviceService.countByTypeAndOwner(Enum.DeviceType.VALIDATOR, user);
            no_readers = deviceService.countByTypeAndOwner(Enum.DeviceType.READER, user);
            no_transactions = transactionService.countByOwner(username);
            //if(role == Enum.Role.AGENT ) {
            try {
                wallet = walletService.findByOwner(user.getUsername());
                card = cardService.findByOwner(user.getUsername());
            } catch (Exception ex) {
                log.info("Caught An Error which happened in Home Controller ===>" + ex.getMessage());
            }
            card_balance = card != null ? card.getBalance() : 0L;
            wallet_balance = wallet != null ? wallet.getBalance() : 0D;
            no_cards = cardService.countByOwner(username);
            no_transfers = transferService.countByOwner(username);
            //}
        } else {
            no_admins = userService.countByRole(username, null, Enum.Role.ISW_ADMIN);
            no_regulators = userService.countByRole(username, null, Enum.Role.REGULATOR);
            no_operators = userService.countByRole(username, null, Enum.Role.OPERATOR);
            no_agents = userService.countByRole(username, null, Enum.Role.AGENT);
            no_service_providers = userService.countByRole(username, null, Enum.Role.SERVICE_PROVIDER);
            no_inspectors = userService.countByRole(username, null, Enum.Role.INSPECTOR);
            no_ticketers = userService.countByRole(username, null, Enum.Role.TICKETER);
            no_drivers = userService.countByRole(username, null, Enum.Role.DRIVER);
            no_vehicles = vehicleService.countAll();
            no_terminals = terminalService.countAll();
            no_routes = routeService.countAll();
            no_validators = deviceService.countByType(Enum.DeviceType.VALIDATOR);
            no_readers = deviceService.countByType(Enum.DeviceType.READER);
            no_transactions = transactionService.countAll();
            no_cards = cardService.countAll();
            no_transfers = transferService.countAll();
            no_trips = tripService.countAll();
        }
        model.addAttribute("no_admins", no_admins);
        model.addAttribute("no_regulators", no_regulators);
        model.addAttribute("no_operators", no_operators);
        model.addAttribute("no_agents", no_agents);
        model.addAttribute("no_service_providers", no_service_providers);
        model.addAttribute("no_inspectors", no_inspectors);
        model.addAttribute("no_ticketers", no_ticketers);
        model.addAttribute("no_drivers", no_drivers);
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
        model.addAttribute("no_trips", no_trips);
        model.addAttribute("no_settlements", no_settlements);

        DateFormat format = new SimpleDateFormat("MMM dd yyyy HH:mm aa");
        Date dateobj = new Date();
        model.addAttribute("time_date", format.format(dateobj));
        return "dashboard";

    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("signupUrl", securityUtil.getSmartmoveSignupUrl());
        return "login";
    }

    @PostMapping("/login")
    public String login(UserLoginRequest user, BindingResult result, Model model, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        String url;
        String token = userService.login(user);
        if (token.equalsIgnoreCase("FirstLogin")) {
            //use the email/username to call the passport endpoint
            redirectAttributes.addFlashAttribute("message", "It appears this is your first login. Please change your password");
            url = "/resetpassword";
        } else if (token.equals("")) {
            redirectAttributes.addFlashAttribute("error", "Incorrect username or password");
            url = "/login";
        } else {
            url = UriComponentsBuilder.fromUriString("/dashboard")
                    .queryParam("auth_token", token)
                    .build().toUriString();
        }
        return "redirect:" + url;
    }

    @GetMapping("/signup")
    public String showSignUp(Principal principal, Model model) {
        model.addAttribute("user", new UserRegistration());
        model.addAttribute("roles", pageUtil.getRoles());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(Principal principal, @Valid UserRegistration user,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            //TODO change findAll to findAllEligible
            model.addAttribute("user", new UserRegistration());
            model.addAttribute("roles", pageUtil.getRoles());
            return "signup";
        }
        String message = userService.selfSignUp(user, principal.getName());
        model.addAttribute("message", message);
        model.addAttribute("user", new UserRegistration());
        return "signup";
    }

    @GetMapping("/changepassword")
    public String showChangePassword() {
        return "changepassword";
    }

    @PostMapping("/changepassword")
    public String changePassword(Principal principal, ChangePassword changePassword, BindingResult result, Model model) throws JsonProcessingException {
        boolean successful = userService.changePassword(principal, changePassword);
        if (successful)
            model.addAttribute("message", "Password change successful");
        else
            model.addAttribute("error", "Password is incorrect");
        return "changepassword";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Logout successful");
        return "redirect:/login";
    }

    @GetMapping("/setCurrency")
    public String setCurrency(Model model, @RequestParam(defaultValue = "NGN") String currency, @RequestParam(defaultValue = "/") String path, HttpSession session) {
        session.setAttribute("currency", currency);
        return "redirect:" + path;
    }

    @GetMapping("/signupnew")
    public String showNewSignupPage(Model model) {
        model.addAttribute("user", new UserRegRequest());
        model.addAttribute("roles", pageUtil.getRoles());
        model.addAttribute("loginUrl", securityUtil.getSmartmoveLoginUrl());
        return "signupnew";
    }

    @PostMapping("/signupnew")
    public String doNewSignupPage(@Valid UserRegRequest user,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", pageUtil.getRoles());
            return "signupnew";
        }

        String message = userService.doSelfSignUp(user);
        model.addAttribute("message", message);
        model.addAttribute("user", new UserRegRequest());
        model.addAttribute("roles", pageUtil.getRoles());
        return "signupnew";
    }

    @GetMapping("/verify")
    public String showEmailVerificationPage(@RequestParam("token") String token, Model model) {
        VerificationToken tokenValidation = userService.getEmailVerificationToken(token);
        //initiate redirect to verification success page
        //redirect to verification failure page
        Enum.EmailVerificationTokenStatus tokenStatus = tokenValidation.getTokenStatus();
        String message = "";
        switch (tokenStatus) {
            case VALID:
                message = String.format("Hello %s,".concat(Enum.EmailVerificationTokenStatus.VALID.getDescription()), tokenValidation.getUser().getFirstName());
                model.addAttribute("message", message);
                return "verificationcheck";
            case EXPIRED:
                message = String.format("Hello %s,".concat(Enum.EmailVerificationTokenStatus.EXPIRED.getDescription()), tokenValidation.getUser().getFirstName());
                model.addAttribute("message", message);
                return "verificationerror";
            default:
                message = String.format("Hello %s,".concat(Enum.EmailVerificationTokenStatus.INVALID.getDescription()), tokenValidation.getUser().getFirstName());
                model.addAttribute("message", message);
                return "verificationerror";
        }
    }

    @GetMapping("/forgotpassword")
    public String showForgetPasswordPage(Model model) {
        // model.addAttribute("user", new UserRegistration());
        return "forgotpassword";
    }

    @GetMapping("/resetpassword")
    public String showResetPasswordPage(Model model) {
        // model.addAttribute("user", new UserRegistration());
        return "resetpassword";
    }
}