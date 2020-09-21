package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.TicketRefund;
import com.interswitch.smartmoveserver.service.TicketRefundService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

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
        Page<TicketRefund> refundPage = refundService.findAllByOperator(principal, page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(refundPage));
        model.addAttribute("refundPage", refundPage);
        return "refunds/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        TicketRefund refund = refundService.findById(id);
        model.addAttribute("refund", refund);
        return "refunds/details";
    }
}
