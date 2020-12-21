package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.AuditTrail;
import com.interswitch.smartmoveserver.service.AuditTrailService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/audit-trails")
public class AuditTrailController {

    @Autowired
    private AuditTrailService auditTrailService;

    @Autowired
    private PageUtil pageUtil;


    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size){
        //TODO:: Implement server side pagination
        //PageView<AuditTrail> auditTrailPageView = auditTrailService.findAllPaginated(owner,  page,  size, principal.getName()) ;
        //model.addAttribute("pageNumbers", pageUtil.getPageNumber(auditTrailPageView));
        List<AuditTrail> auditTrails = auditTrailService.findAll(owner, principal.getName());
        model.addAttribute("auditTrails", auditTrails);
        return "audit-trails/get";
    }
}
