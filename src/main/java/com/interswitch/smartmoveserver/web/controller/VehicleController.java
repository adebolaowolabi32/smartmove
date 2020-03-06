package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @GetMapping("/get")
    public String getAll(Model model, @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(currentPage, pageSize);
        int totalPages = vehiclePage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
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
        model.addAttribute("owners", vehicleService.getAll());
        return "vehicles/create";
    }

    @PostMapping("/create")
    public String create(@Valid Vehicle vehicle, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("owners", vehicleService.getAll());
            return "vehicles/create";
        }
        vehicleService.save(vehicle);

        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(1, 5);
        int totalPages = vehiclePage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("vehiclePage", vehiclePage);
        return "redirect:/vehicles/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("owners", vehicleService.getAll());
        return "vehicles/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Vehicle vehicle,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            vehicle.setId(id);
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("owners", vehicleService.getAll());
            return "vehicles/update";
        }

        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(1, 5);
        int totalPages = vehiclePage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("vehiclePage", vehiclePage);
        return "redirect:/vehicles/get";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        vehicleService.delete(id);
        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(1, 5);
        int totalPages = vehiclePage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("vehiclePage", vehiclePage);
        return "redirect:/vehicles/get";
    }
}
