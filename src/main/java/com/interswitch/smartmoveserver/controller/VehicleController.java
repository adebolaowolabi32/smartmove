package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.User;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String create(Principal principal, @Valid Vehicle vehicle, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("vehicle", vehicle);
            //TODO change findAll to findAllEligible
            model.addAttribute("owners", userService.findAll());
            return "vehicles/create";
        }
        Vehicle savedVehicle = vehicleService.save(vehicle, principal);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/vehicles/details/" + savedVehicle.getId();
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
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        vehicle.setId(id);
        if (result.hasErrors()) {
            //TODO change findAll to findAllEligible
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("owners", userService.findAll());
            return "vehicles/update";
        }
        vehicleService.update(vehicle);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/vehicles/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Vehicle vehicle = vehicleService.findById(id);
        vehicleService.delete(id);
        User owner = vehicle.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/vehicles/get?owner=" + ownerId;
    }
}
