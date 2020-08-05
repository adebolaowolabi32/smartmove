package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.model.request.Transfer;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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
    @GetMapping("/transfer")
    public String transfer(Principal principal, Model model) {
        Transfer transfer = new Transfer();
        model.addAttribute("transfer", transfer);
        model.addAttribute("recipients", userService.findAllByRole(Enum.Role.AGENT));
        return "wallets/transfer";
    }

    @PostMapping("/transfer")
    public String transfer(Principal principal, @Valid Transfer transfer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("transfer", new Transfer());
            return "wallets/transfer";
        }
        User user = userService.findByUsername(principal.getName());
        walletService.transfer(transfer, user);
        model.addAttribute("success", true);
        return "wallets/transfer";
    }
}
