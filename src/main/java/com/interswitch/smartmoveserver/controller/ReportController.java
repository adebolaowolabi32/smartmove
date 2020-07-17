package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Report;
import com.interswitch.smartmoveserver.service.SettlementService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Set;

/*
 * Created by adebola.owolabi on 6/9/2020
 */
@Controller
@Layout(value = "layouts/default")
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private SettlementService settlementService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, Model model) {
        Set<Report> reports = settlementService.getReports(principal.getName());
        model.addAttribute("reports", reports);
        return "settlements/get";
    }

    @GetMapping("/download/{name}")
    public Resource downloadReport(Principal principal, @PathVariable("name") String name) {
        return settlementService.downloadReport(name, principal.getName());
    }
}
