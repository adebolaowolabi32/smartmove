package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.service.TransactionService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String findAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        PageView<Transaction> transactionPage = transactionService.findAllPaginated(page, size, principal.getName());
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(transactionPage));
        model.addAttribute("transactionPage", transactionPage);
        return "transactions/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Transaction transaction = transactionService.findById(id, principal.getName());
        model.addAttribute( "transaction", transaction);
        return "transactions/details";
    }
}

