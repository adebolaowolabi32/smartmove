package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Manifest;
import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.model.Trip;
import com.interswitch.smartmoveserver.service.*;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@Layout(value = "layouts/default")
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ManifestService manifestService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<Trip> tripPage = tripService.findAllPaginated(principal, page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(tripPage));
        model.addAttribute("tripPage", tripPage);
        return "trips/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) {

        Trip trip = tripService.findById(id, principal);

        Page<Manifest> manifestPage = manifestService.findPaginatedManifestByTripId(page, size, trip.getId());
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(manifestPage));
        model.addAttribute("manifestPage", manifestPage);
        model.addAttribute("trip", trip);
        model.addAttribute("count", 0);
        return "trips/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Trip trip = new Trip();
        model.addAttribute("trip", trip);
        //not needed at the moment though,should be removed for performance reasons
        model.addAttribute("drivers", userService.findAllByRole(Enum.Role.DRIVER));
        model.addAttribute("schedules", scheduleService.findAll());
        model.addAttribute("vehicles", vehicleService.findAll());
        return "trips/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Trip trip, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("trip", trip);
            model.addAttribute("drivers", userService.findAllByRole(Enum.Role.DRIVER));
            model.addAttribute("schedules", scheduleService.findAll());
            model.addAttribute("vehicles", vehicleService.findAll());
            return "trips/create";
        }

        Trip savedTrip = tripService.save(trip, principal);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/trips/details/" + savedTrip.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Trip trip = tripService.findById(id, principal);
        model.addAttribute("trip", trip);
        model.addAttribute("drivers", userService.findAllByRole(Enum.Role.DRIVER));
        model.addAttribute("schedules", scheduleService.findAll());
        model.addAttribute("vehicles", vehicleService.findAll());
        return "trips/update";
    }


    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Trip trip,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        trip.setId(id);

        if (result.hasErrors()) {
            model.addAttribute("trip", trip);
            model.addAttribute("drivers", userService.findAllByRole(Enum.Role.DRIVER));
            model.addAttribute("schedules", scheduleService.findAll());
            model.addAttribute("vehicles", vehicleService.findAll());
            return "trips/update";
        }

        tripService.update(trip, principal);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/trips/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Trip trip = tripService.findById(id, principal);
        tripService.delete(id, principal);
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/trips/get";
    }


    @GetMapping("/upload")
    public String showTripUploadPage(Principal principal, Model model) {
        return "trips/upload";
    }


    @PostMapping("/upload")
    public String doTripUpload(Principal principal, MultipartFile file,Model model, RedirectAttributes redirectAttributes) {
        try {
            boolean succeeded = tripService.upload(file);
            redirectAttributes.addFlashAttribute("uploaded", succeeded);
            return "redirect:/trips/get";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", false);
            return "redirect:/trips/get";
        }
    }
}
