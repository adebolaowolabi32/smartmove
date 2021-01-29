package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.repository.WalletRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;


    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public Wallet autoCreateForUser(User user){
        Wallet wallet = new Wallet();
        wallet.setBalance(0);
        wallet.setOwner(user);
        wallet.setEnabled(true);
        return walletRepository.save(wallet);
    }

    public Wallet findById(long id, String principal) {
        return walletRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist"));
    }

    public Wallet findByOwner(String owner) {

        User user = userService.findByUsername(owner);
        if (user != null)
            return walletRepository.findByOwnerId(user.getId());

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }


    public Wallet findByOwner(long ownerId) {
        User user = userService.findById(ownerId);
        if (user != null)
            return walletRepository.findByOwnerId(ownerId);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Wallet update(Wallet wallet, String principal) {
        Optional<Wallet> existing = walletRepository.findById(wallet.getId());
        if(existing.isPresent())
            return walletRepository.save(wallet);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
    }

    public void delete(long id, String principal) {
        Optional<Wallet> existing = walletRepository.findById(id);
        if(existing.isPresent())
            walletRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
        }
    }
}
