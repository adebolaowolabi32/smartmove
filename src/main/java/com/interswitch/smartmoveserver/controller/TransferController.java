package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transfer;
import com.interswitch.smartmoveserver.service.TransferService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/transfers")
public class TransferController {
    @Autowired
    private PageUtil pageUtil;

    @Autowired
    private TransferService transferService;

    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public String findAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                          Model model, @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        //TODO:: Implement server side pagination
        //PageView<Transfer> transferPage = transferService.findAllPaginated(page, size, principal.getName());
        //model.addAttribute("pageNumbers", pageUtil.getPageNumber(transferPage));
        List<Transfer> transfers = transferService.findAll(principal.getName());
        model.addAttribute("transfers", transfers);
        return "transfers/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Transfer transfer = transferService.findById(id);
        model.addAttribute("transfer", transfer);
        return "transfers/details";
    }

    @GetMapping("/transfer")
    public String transfer(Principal principal, Model model) {
        Transfer transfer = new Transfer();
        model.addAttribute("transfer", transfer);
        model.addAttribute("recipients", userService.findAllByRole(principal.getName(), 0L, Enum.Role.AGENT));
        return "transfers/transfer";
    }

    @PostMapping("/transfer")
    public String transfer(Principal principal, @Valid Transfer transfer, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("transfer", new Transfer());
            return "transfers/transfer";
        }
        transferService.transfer(transfer, principal.getName());
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/transfers/get";
    }
}