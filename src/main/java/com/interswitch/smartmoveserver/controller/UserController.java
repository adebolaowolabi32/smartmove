package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.*;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@Controller
@Layout(value = "layouts/default")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    @Autowired
    private CardService cardService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RouteService routeService;



    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam("role") Enum.Role role,
                         @RequestParam(required = false, defaultValue = "0") Long owner,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size, Model model) {
        Page<User> userPage = userService.findAllByRole(principal, owner, role, page, size);
        model.addAttribute("title", pageUtil.buildTitle(role));
        model.addAttribute("role", role);
        model.addAttribute("userPage", userPage);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(userPage));
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(role));
        return "users/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        User user = userService.findById(principal, id);
        model.addAttribute("regulators_no", userService.countByRoleAndOwner(user, Enum.Role.REGULATOR));
        model.addAttribute("operators_no", userService.countByRoleAndOwner(user, Enum.Role.OPERATOR));
        model.addAttribute("agents_no", userService.countByRoleAndOwner(user, Enum.Role.AGENT));
        model.addAttribute("vehicles_no", vehicleService.countByOwner(user));
        model.addAttribute("terminals_no", terminalService.countByOwner(user));
        model.addAttribute("routes_no", routeService.countByOwner(user));
        model.addAttribute("devices_no", deviceService.countByOwner(user));
        model.addAttribute("transactions_no", transactionService.countAll());
        model.addAttribute("settlements_no", 0);
        model.addAttribute("cards_no", cardService.countByOwner(user));

        model.addAttribute("title", pageUtil.buildTitle(user.getRole()));
        model.addAttribute("user", user);
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(user.getRole()));
        //TODO::Get children of each entity by type
        return "users/details";
    }


    @GetMapping("/create")
    public String showCreate(Principal principal, @RequestParam("role") Enum.Role role, Model model) {
        User user = new User();
        user.setRole(role);
        model.addAttribute("title", pageUtil.buildTitle(role));
        model.addAttribute("user", user);
        //TODO change findAll to findAllEligible
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(role));
        model.addAttribute("owners", userService.findAll());

        return "users/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @RequestParam("role") Enum.Role role, @Valid User user, BindingResult result, Model model) {
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(role));
        if (result.hasErrors()) {
            model.addAttribute("title", pageUtil.buildTitle(user.getRole()));
            //TODO change findAll to findAllEligible
            model.addAttribute("owners", userService.findAll());
            model.addAttribute("user", user);
            return "users/create";
        }
        user.setRole(role);
        userService.save(user, principal);
        Page<User> userPage = userService.findAllByRole(principal, 0, role, 1, 10);
        model.addAttribute("title", pageUtil.buildTitle(role));
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(userPage));
        model.addAttribute("userPage", user);
        model.addAttribute("saved", true);
        return "redirect:/users/get?role=" + role;
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        User user = userService.findById(principal, id);
        model.addAttribute("title", pageUtil.buildTitle(user.getRole()));
        model.addAttribute("user", user);
        //TODO change findAll to findAllEligible
        model.addAttribute("owners", userService.findAll());
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(user.getRole()));
        return "users/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid User user,
                         BindingResult result, Model model) {
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(user.getRole()));
        if (result.hasErrors()) {
            model.addAttribute("title", pageUtil.buildTitle(user.getRole()));
            model.addAttribute("user", user);
            //TODO change findAll to findAllEligible
            model.addAttribute("owners", userService.findAll());
            return "users/update";
        }
        userService.update(user);
        model.addAttribute("title", pageUtil.buildTitle(user.getRole()));
        model.addAttribute("updated", true);
        return "redirect:/users/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        userService.delete(id);
        Page<User> userPage = userService.findAll(1, 10);
        model.addAttribute("userPage", userPage);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(userPage));
        Enum.Role role = user.getRole();
        model.addAttribute("title", pageUtil.buildTitle(role));
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(role));
        model.addAttribute("deleted", true);
        return "redirect:/users/get?role=" + role;
    }
}
