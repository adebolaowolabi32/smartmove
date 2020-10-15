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
import org.springframework.http.HttpStatus;
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

    @Autowired
    private TripService tripService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ManifestService manifestService;

    private final Log logger = LogFactory.getLog(getClass());


    @GetMapping("/upload/{source}/{id}")
    public String showManifestUploadPage(Principal principal, @PathVariable("id") long id, @PathVariable("source") String originationPage, Model model) {

        logger.info("Entered manifest controller==>ID " + id + " and source==>" + originationPage);

        List<Manifest> manifests = new ArrayList<>();
        model.addAttribute("manifests", manifests);

        if (originationPage.equalsIgnoreCase("trip")) {
            Trip trip = tripService.findById(id);
            model.addAttribute("trip",trip);
        }

        if (originationPage.equalsIgnoreCase("schedule")) {
            Schedule schedule = scheduleService.findById(id);
            model.addAttribute("schedule",schedule);
        }

        return "manifests/upload";
    }

    @PostMapping("/upload/{source}/{id}")
    public String uploadManifest(Principal principal,@PathVariable("id") long id,
                                 @PathVariable("source") String originationPage,
                                 MultipartFile file, Model model,
                                 RedirectAttributes redirectAttributes) {
        try {

            List<Manifest> manifestList = new ArrayList<>();

            if (originationPage.equalsIgnoreCase("trip")) {
                manifestList  =  manifestService.upload(file,null,null);
            }

            if(originationPage.equalsIgnoreCase("schedule")){
                manifestList  =  manifestService.upload(file,null,null);
            }

            redirectAttributes.addFlashAttribute("updated", true);
            return originationPage.equalsIgnoreCase("trip") ? "redirect:/trips/details/" + id : "redirect:/schedules/details/"+ id;

        } catch (IOException ex) {
            logger.error("Error happened trying to upload manifest==>"+ex.getMessage());
            return "";
        }

    }
}
