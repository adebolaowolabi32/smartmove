package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Ticket;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.view.TicketDetails;
import com.interswitch.smartmoveserver.service.TicketService;
import com.interswitch.smartmoveserver.service.UserService;
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

/*
 * Created by adebola.owolabi on 7/27/2020
 */
@Controller
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {

        User user = userService.findByUsername(principal.getName());
        Page<Ticket> ticketPage = ticketService.findAllByOperator(principal, page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(ticketPage));
        model.addAttribute("ticketPage", ticketPage);
        model.addAttribute("status",user.getTillStatus().name());
        return "tickets/get";
    }

    @GetMapping("/pass")
    public String showBoardingPass(Principal principal, Model model) {
        TicketDetails ticketDetails = new TicketDetails();
        model.addAttribute("ticketDetails", ticketDetails);
        model.addAttribute("owners", userService.findAll());
        return "tickets/pass";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Ticket ticket = ticketService.findById(id);
        model.addAttribute("ticket", ticket);
        return "tickets/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Ticket ticket = new Ticket();
        model.addAttribute("ticket", ticket);
        model.addAttribute("owners", userService.findAll());
        return "tickets/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Ticket ticket, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("ticket", ticket);
            model.addAttribute("owners", userService.findAll());
            return "tickets/create";
        }

        Ticket savedTicket = ticketService.save(principal, ticket);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/tickets/details/" + savedTicket.getId();
    }
}
