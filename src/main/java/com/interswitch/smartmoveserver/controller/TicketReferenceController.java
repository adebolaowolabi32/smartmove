package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.TicketReference;
import com.interswitch.smartmoveserver.service.TicketReferenceService;
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
@RequestMapping("/ticketReference")
public class TicketReferenceController {

    @Autowired
    private TicketReferenceService ticketReferenceService;

    @GetMapping("/details")
    public String getDetails(Principal principal, Model model) {
        TicketReference ticketReference = ticketReferenceService.findByOwner(principal.getName());
        model.addAttribute("ticketReference", ticketReference);
        return "ticketReference/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        TicketReference ticketReference = new TicketReference();
        model.addAttribute("ticketReference", ticketReference);
        return "ticketReference/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid TicketReference ticketReference, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("ticketReference", ticketReference);
            return "ticketReference/create";
        }

        ticketReferenceService.save(ticketReference, principal.getName());
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/ticketReference/details/";
    }

    @GetMapping("/update")
    public String showUpdate(Principal principal, Model model) {
        TicketReference ticketReference = ticketReferenceService.findByOwner(principal.getName());
        model.addAttribute("ticketReference", ticketReference);
        return "ticketReference/update";
    }


    @PostMapping("/update")
    public String update(Principal principal, @Valid TicketReference ticketReference,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("ticketReference", ticketReference);
            return "ticketReference/update";
        }

        ticketReferenceService.update(ticketReference, principal.getName());
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/ticketReference/details/";
    }

    @GetMapping("/delete")
    public String delete(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        ticketReferenceService.delete(principal.getName());
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/ticketReference/details";
    }
}
