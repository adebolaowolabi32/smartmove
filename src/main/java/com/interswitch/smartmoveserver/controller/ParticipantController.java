package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Participant;
import com.interswitch.smartmoveserver.service.ParticipantService;
import com.interswitch.smartmoveserver.service.SchemeService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

/*
 * Created by adebola.owolabi on 6/18/2020
 */
@Controller
@Layout(value = "layouts/default")
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, Model model) {
        Set<Participant> participants = participantService.getParticipants();
        model.addAttribute("participants", participants);
        return "participants/get";
    }

    @GetMapping("/get/{schemeId}")
    public String getForScheme(Principal principal, @PathVariable("schemeId") long schemeId, Model model) {
        Set<Participant> participants = participantService.getParticipantsForScheme(String.valueOf(schemeId));
        model.addAttribute("participants", participants);
        return "participants/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Participant participant = participantService.findParticipant(String.valueOf(id));
        model.addAttribute("participant", participant);
        return "participants/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Participant participant = new Participant();
        model.addAttribute("participant", participant);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("schemes", schemeService.getSchemes());
        return "participants/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Participant participant, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("participant", participant);
            model.addAttribute("users", userService.findAll());
            model.addAttribute("schemes", schemeService.getSchemes());
            return "participants/create";
        }
        participantService.createParticipant(participant);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/participants/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") int id, Model model) {
        Participant participant = participantService.findParticipant(String.valueOf(id));
        model.addAttribute("participant", participant);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("schemes", schemeService.getSchemes());
        return "participants/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") int id, @Valid Participant participant,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        participant.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("participant", participant);
            model.addAttribute("users", userService.findAll());
            model.addAttribute("schemes", schemeService.getSchemes());
            return "participants/update";
        }
        participantService.updateParticipant(participant);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/participants/details/" + id;
    }


    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        participantService.deleteParticipant(String.valueOf(id));
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/participants/get";
    }
}
