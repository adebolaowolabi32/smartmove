package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.service.RouteService;
import com.interswitch.smartmoveserver.service.TerminalService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.PageUtil;
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
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<Route> routePage = routeService.findAllPaginated(page, size);
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
        model.addAttribute("terminals", terminalService.getAll());
        return "routes/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Route route, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("route", route);
            model.addAttribute("owners", userService.findAll());
            model.addAttribute("terminals", terminalService.getAll());
            return "routes/create";
        }
        routeService.save(route, principal);
        Page<Route> routePage = routeService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(routePage));
        model.addAttribute("routePage", routePage);
        model.addAttribute("saved", true);
        return "redirect:/routes/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Route route = routeService.findById(id);
        model.addAttribute("route", route);
        model.addAttribute("owners", userService.findAll());
        model.addAttribute("terminals", terminalService.getAll());
        return "routes/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Route route,
                         BindingResult result, Model model) {
        route.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("route", route);
            model.addAttribute("owners", userService.findAll());
            model.addAttribute("terminals", terminalService.getAll());
            return "routes/update";
        }
        routeService.update(route);
        model.addAttribute("updated", true);
        return "redirect:/routes/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model) {
        routeService.delete(id);
        Page<Route> routePage = routeService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(routePage));
        model.addAttribute("routePage", routePage);
        model.addAttribute("deleted", true);
        return "redirect:/routes/get";
    }
}
