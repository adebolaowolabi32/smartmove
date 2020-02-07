package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.request.LogTransaction;
import com.interswitch.smartmoveserver.model.response.LogTransactionResponse;
import com.interswitch.smartmoveserver.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public LogTransactionResponse logTransaction(@RequestBody @Validated LogTransaction logTransaction) {
        return transactionService.saveTransaction(logTransaction);
    }

    @GetMapping(value = "/", produces = "application/json")
    public LogTransactionResponse getTransactions(@Validated LogTransaction logTransaction) {
       //get all transactions for an owner, bus, operator, agent, isw admin etc
        return new LogTransactionResponse();
    }
}
