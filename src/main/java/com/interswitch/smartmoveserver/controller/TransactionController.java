package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Transaction saveTransaction(@RequestBody @Validated Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }

    @GetMapping(produces = "application/json")
    public Iterable<Transaction> getAll() {
        return transactionService.getAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Transaction findById(@Validated @PathVariable long id) {
        return transactionService.findById(id);
    }

    @GetMapping(value = "/findByType/{type}", produces = "application/json")
    public Iterable<Transaction> find(@Validated @PathVariable Enum.TransactionType type) {
        return transactionService.find(type);
    }

    @GetMapping(value = "/findBySender/{senderId}", produces = "application/json")
    public Iterable<Transaction> findBySender(@Validated @PathVariable long senderId) {
        return transactionService.findBySender(senderId);
    }

    @GetMapping(value = "/findByRecipient/{recipientId}", produces = "application/json")
    public Iterable<Transaction> findByRecipient(@Validated @PathVariable long recipientId) {
        return transactionService.findByRecipient(recipientId);
    }
    @GetMapping(value = "/findByDevice/{deviceId}", produces = "application/json")
    public Iterable<Transaction> findByDevice(@Validated @PathVariable long deviceId) {
        return transactionService.findByDevice(deviceId);
    }
    @GetMapping(value = "/findByCardNumber/{cardNumber}", produces = "application/json")
    public Iterable<Transaction> findByCardNumber(@Validated @PathVariable String cardNumber) {
        return transactionService.findByCardNumber(cardNumber);
    }
}
