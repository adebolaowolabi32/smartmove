/*
package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.service.TransactionService;
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

*/
/**
 * @author adebola.owolabi
 *//*

@Controller
@RequestMapping("/topups")
public class TopupController {
    @Autowired
    private TransactionService topupService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String findAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                          Model model, @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        Page<Topup> topupPage = topupService.findAllPaginated(page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(topupPage));
        model.addAttribute("topupPage", topupPage);
        return "topups/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Topup topup = topupService.findById(id);
        model.addAttribute( "topup", topup);
        return "topups/details";
    }
}

*/
