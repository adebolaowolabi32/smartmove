package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Manifest;
import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.service.*;
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

@Controller
@Layout(value = "layouts/default")
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private ManifestService manifestService;

    @Autowired
    private VehicleCategoryService vehicleCategoryService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<Schedule> schedulePage = scheduleService.findAllPaginated(principal, page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(schedulePage));
        model.addAttribute("schedulePage", schedulePage);
        return "schedules/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) {

        Schedule schedule = scheduleService.findById(id, principal);

        Page<Manifest> manifestPage = manifestService.findPaginatedManifestByScheduleId(page, size, schedule.getId());
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(manifestPage));
        model.addAttribute("manifestPage", manifestPage);
        model.addAttribute("schedule", schedule);
        return "schedules/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Schedule schedule = new Schedule();
        model.addAttribute("schedule", schedule);
        //not needed at the moment though,should be removed for performance reasons
        model.addAttribute("owners", userService.findAll());
        model.addAttribute("terminals", terminalService.findAll());
        model.addAttribute("vehicles", vehicleCategoryService.findAll());
        return "schedules/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Schedule schedule, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("schedule", schedule);
            model.addAttribute("owners", userService.findAll());
            model.addAttribute("terminals", terminalService.findAll());
            model.addAttribute("vehicles", vehicleCategoryService.findAll());
            return "schedules/create";
        }

        Schedule savedSchedule = scheduleService.save(schedule, principal);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/schedules/details/" + savedSchedule.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Schedule schedule = scheduleService.findById(id, principal);
        model.addAttribute("schedule", schedule);
        model.addAttribute("owners", userService.findAll());
        model.addAttribute("terminals", terminalService.findAll());
        model.addAttribute("vehicles", vehicleCategoryService.findAll());
        return "schedules/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Schedule schedule,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        schedule.setId(id);

        if (result.hasErrors()) {
            model.addAttribute("schedule", schedule);
            model.addAttribute("owners", userService.findAll());
            model.addAttribute("terminals", terminalService.findAll());
            model.addAttribute("vehicles", vehicleCategoryService.findAll());
            return "schedules/update";
        }

        scheduleService.update(schedule, principal);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/schedules/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Schedule schedule = scheduleService.findById(id, principal);
        scheduleService.delete(id, principal);
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/schedules/get";
    }
}
