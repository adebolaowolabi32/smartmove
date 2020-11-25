package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.TripReference;
import com.interswitch.smartmoveserver.service.TripReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@Layout(value = "layouts/default")
@RequestMapping("/tripReference")
public class TripReferenceController {

    @Autowired
    private TripReferenceService tripReferenceService;

    @GetMapping("/details")
    public String getDetails(Principal principal, Model model) {
        TripReference tripReference = tripReferenceService.findByOwner(principal.getName());
        model.addAttribute("tripReference", tripReference);
        return "tripReference/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        TripReference tripReference = new TripReference();
        model.addAttribute("tripReference", tripReference);
        return "tripReference/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid TripReference tripReference, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tripReference", tripReference);
            return "tripReference/create";
        }

        tripReferenceService.save(tripReference, principal.getName());
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/tripReference/details/";
    }

    @GetMapping("/update")
    public String showUpdate(Principal principal, Model model) {
        TripReference tripReference = tripReferenceService.findByOwner(principal.getName());
        model.addAttribute("tripReference", tripReference);
        return "tripReference/update";
    }


    @PostMapping("/update")
    public String update(Principal principal, @Valid TripReference tripReference,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tripReference", tripReference);
            return "tripReference/update";
        }

        tripReferenceService.update(tripReference, principal.getName());
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/tripReference/details/";
    }

    @GetMapping("/delete")
    public String delete(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        tripReferenceService.delete(principal.getName());
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/tripReference/details";
    }
}
