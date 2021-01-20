package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.service.WalletService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import com.sun.mail.iap.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private Wallet save(@Validated @RequestBody Wallet wallet) {
        return walletService.save(wallet);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Wallet findById(@Validated @PathVariable long id) {
        return walletService.findById(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/balance", produces = "application/json")
    private Wallet findByOwner(@RequestParam("username") String username) {
        return walletService.findByOwner(username);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Wallet update(@Validated @RequestBody Wallet wallet) {
        return walletService.update(wallet,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        walletService.delete(id,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}
