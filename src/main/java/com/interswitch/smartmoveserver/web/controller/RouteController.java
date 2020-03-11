package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.service.RouteService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.web.util.PageUtil;
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
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<Route> routePage = routeService.findAllPaginated(page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(routePage));
        model.addAttribute("routePage", routePage);
        return "routes/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        Route route = routeService.findById(id);
        model.addAttribute("route", route);
        return "routes/details";
    }

    @GetMapping("/create")
    public String showCreate(Model model) {
        Route route = new Route();
        model.addAttribute("route", route);
        model.addAttribute("owners", userService.getAll());
        return "routes/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Route route, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("route", route);
            model.addAttribute("owners", userService.getAll());
            return "routes/create";
        }
        routeService.save(route, principal);
        Page<Route> routePage = routeService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(routePage));
        model.addAttribute("routePage", routePage);
        return "redirect:/routes/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Route route = routeService.findById(id);
        model.addAttribute("route", route);
        model.addAttribute("owners", userService.getAll());
        return "routes/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Route route,
                         BindingResult result, Model model) {
        route.setId(id);
        model.addAttribute("route", route);
        if (result.hasErrors()) {
            model.addAttribute("owners", userService.getAll());
            return "routes/update";
        }
        routeService.update(route);
        return "redirect:/routes/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        routeService.delete(id);
        Page<Route> routePage = routeService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(routePage));
        model.addAttribute("routePage", routePage);
        return "redirect:/routes/get";
    }
}
