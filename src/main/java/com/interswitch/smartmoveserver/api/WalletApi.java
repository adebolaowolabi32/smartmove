package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/wallets")
public class WalletApi {
    @Autowired
    WalletService walletService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Wallet save(@Validated @RequestBody Wallet wallet, Principal principal) {
        return walletService.save(wallet);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Wallet findById(@Validated @PathVariable long id, Principal principal) {
        return walletService.findById(id, principal);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Wallet update(@Validated @RequestBody Wallet wallet, Principal principal) {
        return walletService.update(wallet, principal);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id, Principal principal) {
        walletService.delete(id, principal);
    }
}
