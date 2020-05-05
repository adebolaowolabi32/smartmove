package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.VehicleService;
import com.interswitch.smartmoveserver.service.WebViewService;
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
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @Autowired
    UserService userService;

    @Autowired
    WebViewService webViewService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size, Model model) {
        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(principal, owner, page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(vehiclePage));
        model.addAttribute("vehiclePage", vehiclePage);
        return "vehicles/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Vehicle vehicle = vehicleService.findById(principal, id);
        model.addAttribute("vehicle", vehicle);
        return "vehicles/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Vehicle vehicle = new Vehicle();
        model.addAttribute("vehicle", vehicle);
        //TODO change findAll to findAllEligible
        model.addAttribute("owners", userService.findAll());
        return "vehicles/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Vehicle vehicle, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("vehicle", vehicle);
            //TODO change findAll to findAllEligible
            model.addAttribute("owners", userService.findAll());
            return "vehicles/create";
        }
        vehicleService.save(vehicle, principal);
        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(principal);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(vehiclePage));
        model.addAttribute("vehiclePage", vehiclePage);
        model.addAttribute("saved", true);
        return "redirect:/vehicles/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Vehicle vehicle = vehicleService.findById(principal, id);
        model.addAttribute("vehicle", vehicle);
        //TODO change findAll to findAllEligible
        model.addAttribute("owners", userService.findAll());
        return "vehicles/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Vehicle vehicle,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("vehicle", vehicle);
            //TODO change findAll to findAllEligible
            model.addAttribute("owners", userService.findAll());
            return "vehicles/update";
        }
        vehicleService.update(vehicle);
        model.addAttribute("updated", true);
        return "redirect:/vehicles/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model) {
        vehicleService.delete(id);
        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(principal);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(vehiclePage));
        model.addAttribute("vehiclePage", vehiclePage);
        model.addAttribute("deleted", true);
        return "redirect:/vehicles/get";
    }
}
