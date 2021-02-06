package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.RouteService;
import com.interswitch.smartmoveserver.service.TerminalService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.VehicleService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private UserService userService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        //TODO:: Implement server side pagination
        List<Route> routes = routeService.findAll(owner, principal.getName());
        model.addAttribute("routes", routes);
        return "routes/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Route route = routeService.findById(id, principal.getName());
        model.addAttribute("route", route);
        return "routes/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Route route = new Route();
        model.addAttribute("route", route);
        model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("route")));
        model.addAttribute("terminals", terminalService.findAll(0L, principal.getName()));
        model.addAttribute("vehicles", vehicleService.findByOwner(principal.getName()));
        return "routes/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Route route, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("route", route);
            model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("route")));
            model.addAttribute("terminals", terminalService.findAll(0L, principal.getName()));
            model.addAttribute("vehicles", vehicleService.findByOwner(principal.getName()));
            return "routes/create";
        }

        Route savedRoute = routeService.save(route, principal.getName());
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/routes/details/" + savedRoute.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Route route = routeService.findById(id, principal.getName());
        model.addAttribute("route", route);
        model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("route")));
        model.addAttribute("terminals", terminalService.findAll(0L, principal.getName()));
        model.addAttribute("vehicles", vehicleService.findByOwner(principal.getName()));
        return "routes/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Route route,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        route.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("route", route);
            model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("route")));
            model.addAttribute("terminals", terminalService.findAll(0L, principal.getName()));
            model.addAttribute("vehicles", vehicleService.findByOwner(principal.getName()));
            return "routes/update";
        }
        routeService.update(route, principal.getName());
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/routes/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Route route = routeService.findById(id, principal.getName());
        routeService.delete(id, principal.getName());
        User owner = route.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/routes/get?owner=" + ownerId;
    }
}
