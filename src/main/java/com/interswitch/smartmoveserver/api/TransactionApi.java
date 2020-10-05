package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionApi {
    @Autowired
    private TransactionService transactionService;

    @GetMapping(produces = "application/json")
    public Page<Transaction> findAll(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = transactionService.findAllPaginated(principal, page, size);
        return new Page<Transaction>(pageable.getTotalElements(), pageable.getContent());    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Transaction findById(@Validated @PathVariable long id, Principal principal) {
        return transactionService.findById(id, principal);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Transaction save(@RequestBody @Validated Transaction transaction, Principal principal) {
        return transactionService.save(transaction, principal);
    }
}
