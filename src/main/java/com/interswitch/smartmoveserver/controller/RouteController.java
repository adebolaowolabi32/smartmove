package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.RouteService;
import com.interswitch.smartmoveserver.service.TerminalService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.VehicleService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/routes")
public class RouteController {

    private final Log logger = LogFactory.getLog(getClass());

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
        Page<Route> routePage = routeService.findAllPaginated(principal, owner, page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(routePage));
        model.addAttribute("routePage", routePage);
        return "routes/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Route route = routeService.findById(id);
        model.addAttribute("route", route);
        return "routes/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Route route = new Route();
        model.addAttribute("route", route);
        model.addAttribute("owners", userService.findAll());
        model.addAttribute("terminals", terminalService.findAll());
        model.addAttribute("vehicles", vehicleService.findAll());
        return "routes/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Route route, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("route", route);
            model.addAttribute("owners", userService.findAll());
            model.addAttribute("terminals", terminalService.findAll());
            model.addAttribute("vehicles", vehicleService.findAll());
            return "routes/create";
        }

        Route savedRoute = routeService.save(route, principal);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/routes/details/" + savedRoute.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Route route = routeService.findById(id);
        model.addAttribute("route", route);
        model.addAttribute("owners", userService.findAll());
        model.addAttribute("terminals", terminalService.findAll());
        model.addAttribute("vehicles", vehicleService.findAll());
        return "routes/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Route route,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        route.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("route", route);
            model.addAttribute("owners", userService.findAll());
            model.addAttribute("terminals", terminalService.findAll());
            model.addAttribute("vehicles", vehicleService.findAll());
            return "routes/update";
        }
        routeService.update(route);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/routes/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Route route = routeService.findById(id);
        routeService.delete(id);
        User owner = route.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/routes/get?owner=" + ownerId;
    }
}
