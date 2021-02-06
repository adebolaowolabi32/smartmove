package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.TicketRefund;
import com.interswitch.smartmoveserver.service.TicketRefundService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/*
 * Created by adebola.owolabi on 7/27/2020
 */
@Controller
@RequestMapping("/refunds")
public class RefundController {
    @Autowired
    PageUtil pageUtil;
    @Autowired
    private TicketRefundService refundService;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        //TODO:: Implement server side pagination
        List<TicketRefund> refunds = refundService.findAllByOwner(principal.getName());
        model.addAttribute("refunds", refunds);
        return "refunds/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        TicketRefund refund = refundService.findById(id);
        model.addAttribute("refund", refund);
        return "refunds/details";
    }
}
