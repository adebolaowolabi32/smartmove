package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Manifest;
import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.model.Trip;
import com.interswitch.smartmoveserver.service.ManifestService;
import com.interswitch.smartmoveserver.service.ScheduleService;
import com.interswitch.smartmoveserver.service.TripService;
import com.interswitch.smartmoveserver.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@Layout(value = "layouts/default")
@RequestMapping("/manifests")
public class ManifestController {

    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private TripService tripService;
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ManifestService manifestService;

    @GetMapping("/upload-schedule-manifest/{id}")
    public String showManifestUploadPage(Principal principal, @PathVariable("id") long id, Model model) {
        Schedule schedule = scheduleService.findById(id);
        model.addAttribute("schedule", schedule);
        return "manifests/upload-schedule-manifest";
    }

    @PostMapping("/upload-schedule-manifest/{id}")
    public String uploadManifest(Principal principal, @PathVariable("id") long id,
                                 MultipartFile file, Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            List<Manifest> manifestList = new ArrayList<>();
            manifestList = manifestService.upload(file, null, scheduleService.findById(id));
            redirectAttributes.addFlashAttribute("updated", true);
            return "redirect:/schedules/details/" + id;
        } catch (Exception ex) {
            logger.error("Error happened trying to upload manifest==>" + ex.getMessage());
            redirectAttributes.addFlashAttribute("error", true);
            return "redirect:/schedules/details/" + id;
        }

    }

    @GetMapping("/upload-trip-manifest/{id}")
    public String showTripManifestUploadPage(Principal principal, @PathVariable("id") long id, Model model) {
        Trip trip = tripService.findById(id);
        model.addAttribute("trip", trip);
        return "manifests/upload-trip-manifest";
    }

    @PostMapping("/upload-trip-manifest/{id}")
    public String uploadTripManifest(Principal principal, @PathVariable("id") long id,
                                     MultipartFile file, Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            List<Manifest> manifestList = new ArrayList<>();
            manifestList = manifestService.upload(file, tripService.findById(id), null);
            redirectAttributes.addFlashAttribute("updated", true);
            return "redirect:/trips/details/" + id;
        } catch (IOException ex) {
            logger.error("Error happened trying to upload manifest==>" + ex.getMessage());
            return "redirect:/trips/details/" + id;
        }

    }
}
