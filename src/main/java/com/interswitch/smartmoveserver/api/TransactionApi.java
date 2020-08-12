package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionApi {
    @Autowired
    private TransactionService transactionService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Transaction saveTransaction(@RequestBody @Validated Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }

    @GetMapping(produces = "application/json")
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Transaction findById(@Validated @PathVariable long id) {
        return transactionService.findById(id);
    }

    @GetMapping(value = "/findByType/{type}", produces = "application/json")
    public List<Transaction> find(@Validated @PathVariable Enum.TransactionType type) {
        return transactionService.find(type);
    }

    @GetMapping(value = "/findByOperator/{operatorId}", produces = "application/json")
    public List<Transaction> findByRecipient(@Validated @PathVariable String operatorId) {
        return transactionService.findByOperator(operatorId);
    }
}
