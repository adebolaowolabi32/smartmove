package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.VehicleService;
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
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @Autowired
    UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "5") int size) {
        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(vehiclePage));
        model.addAttribute("vehiclePage", vehiclePage);
        return "vehicles/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id);
        model.addAttribute("vehicle", vehicle);
        return "vehicles/details";
    }

    @GetMapping("/create")
    public String showCreate(Model model) {
        Vehicle vehicle = new Vehicle();
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("owners", userService.getAll());
        return "vehicles/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Vehicle vehicle, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("owners", userService.getAll());
            return "vehicles/create";
        }
        vehicleService.save(vehicle, principal);

        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(vehiclePage));
        model.addAttribute("vehiclePage", vehiclePage);
        return "redirect:/vehicles/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("owners", userService.getAll());
        return "vehicles/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Vehicle vehicle,
                         BindingResult result, Model model) {
        vehicle.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("owners", userService.getAll());
            return "vehicles/update";
        }
        vehicleService.update(vehicle);
        return "redirect:/vehicles/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        vehicleService.delete(id);
        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(vehiclePage));
        model.addAttribute("vehiclePage", vehiclePage);
        return "redirect:/vehicles/get";
    }
}
