package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/routes")
public class RouteController {
    @Autowired
    RouteService routeService;

    @GetMapping("/get")
    public String getAll(Model model) {
        model.addAttribute("routes", routeService.getAll());
        return "getroutes";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        Route route = routeService.findById(id);
        model.addAttribute("route", route);
        return "getroutedetails";
    }

    @GetMapping("/add")
    public String showCreate(Model model) {
        return "createroute";
    }

    @PostMapping("/add")
    public String create(@Valid Route route, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create-route";
        }
        routeService.save(route);
        model.addAttribute("routes", routeService.getAll());
        return "getroutes";
    }

    @GetMapping("/edit/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Route route = routeService.findById(id);
        model.addAttribute("route", route);
        return "updateroute";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") long id, @Valid Route route,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            route.setId(id);
            return "updateroute";
        }

        routeService.update(route);
        model.addAttribute("routes", routeService.getAll());
        return "getroutes";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        routeService.delete(id);
        model.addAttribute("routes", routeService.getAll());
        return "getroutes";
    }
}
