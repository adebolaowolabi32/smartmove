package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.WalletTransfer;
import com.interswitch.smartmoveserver.service.TransferService;
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

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/transfers")
public class TransferController {
    @Autowired
    PageUtil pageUtil;
    @Autowired
    private TransferService transferService;

    @GetMapping("/get")
    public String findAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                          Model model, @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        Page<WalletTransfer> transferPage = transferService.findAllPaginated(page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(transferPage));
        model.addAttribute("transferPage", transferPage);
        return "wallets/transfers";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        WalletTransfer transfer = transferService.findById(id);
        model.addAttribute("transfer", transfer);
        return "wallets/details";
    }
}