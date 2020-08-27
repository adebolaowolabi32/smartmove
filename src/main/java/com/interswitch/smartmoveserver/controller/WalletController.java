package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/wallets")
public class WalletController {
    @Autowired
    WalletService walletService;

    @Autowired
    UserService userService;

    @GetMapping("/details")
    public String getDetails(Principal principal, Model model) {
        Wallet wallet = walletService.findByOwner(principal.getName());
        model.addAttribute("wallet", wallet);
        return "wallets/details";
    }
}
