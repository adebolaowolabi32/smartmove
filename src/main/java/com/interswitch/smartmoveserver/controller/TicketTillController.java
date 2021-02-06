package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.TicketTillSummary;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.view.TicketTillView;
import com.interswitch.smartmoveserver.service.TicketTillService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.DateUtil;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@Layout(value = "layouts/default")
@RequestMapping("/ticket-till")
public class TicketTillController {

    @Autowired
    TicketTillService ticketTillService;
    @Autowired
    UserService userService;
    @Autowired
    PageUtil pageUtil;

    @GetMapping("/status")
    public String showTicketTillStatus(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName());
        TicketTillView ticketTill = ticketTillService.findCurrentUserTicketTillStatus(user);
        model.addAttribute("status", user.getTillStatus().name());
        model.addAttribute("ticketTill", ticketTill);
        model.addAttribute("todayDate", DateUtil.getTodayDate());
        return "ticket-till/status";
    }

    @PostMapping("/close")
    public String closeTicketTill(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        TicketTillView ticketTill = ticketTillService.findCurrentUserTicketTillStatus(user);
        ticketTillService.closeTicketTill(ticketTill);
        return "redirect:/ticket-till/status";
    }

    @GetMapping("/approval/{id}")
    public String approveTicketTill(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName());
        ticketTillService.approveTicketTill(user, id);
        redirectAttributes.addFlashAttribute("approved", true);
        return "redirect:/ticket-till/approval";
    }

    @GetMapping("/approval")
    public String showApproveTicketTill(Principal principal, @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size, Model model) {
        User user = userService.findByUsername(principal.getName());
        PageView<TicketTillSummary> ticketTillSummaryPage = ticketTillService.findUnApprovedTicketTillSummary(user.getId(), user.getOwner() != null ? user.getOwner().getId() : 0, false, page, size);
        model.addAttribute("ticketTillSummaryPage", ticketTillSummaryPage);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(ticketTillSummaryPage));
        return "ticket-till/approval";
    }

    @GetMapping("/approved")
    public String showApprovedTicketTill(Principal principal, @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size, Model model) {
        User user = userService.findByUsername(principal.getName());
        PageView<TicketTillSummary> ticketTillSummaryPage = ticketTillService.findUnApprovedTicketTillSummary(user.getId(), user.getOwner() != null ? user.getOwner().getId() : 0, true, page, size);
        model.addAttribute("ticketTillSummaryPage", ticketTillSummaryPage);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(ticketTillSummaryPage));
        return "ticket-till/approved";
    }
}
