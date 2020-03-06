package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.model.request.Transfer;
import com.interswitch.smartmoveserver.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/wallets")
public class WalletApi {
    @Autowired
    WalletService walletService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Wallet save(@Validated @RequestBody Wallet wallet) {
        return walletService.save(wallet);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Wallet findById(@Validated @PathVariable long id) {
        return walletService.findById(id);
    }

    @GetMapping(value = "/findByOwner/{owner}", produces = "application/json")
    private Wallet findByOwner(@Validated @PathVariable String owner) {
        return walletService.findByOwner(owner);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Wallet update(@Validated @RequestBody Wallet wallet) {
        return walletService.update(wallet);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        walletService.delete(id);
    }

    @PostMapping(value = "/transfer", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private void transfer(@Validated @RequestBody Transfer transferObj) {
        walletService.transfer(transferObj);
    }

    @PostMapping(value = "/activate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void activate(@Validated @PathVariable long id) {
        walletService.activate(id);
    }

    @PostMapping(value = "/deactivate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void deactivate(@Validated @PathVariable long id) {
        walletService.deactivate(id);
    }
}
