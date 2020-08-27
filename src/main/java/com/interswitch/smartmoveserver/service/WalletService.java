package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.WalletRepository;
import com.interswitch.smartmoveserver.repository.WalletTransferRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
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
    private WalletTransferRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransferService transferService;

    @Autowired
    PageUtil pageUtil;

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

    public Wallet findById(long id) {
        return walletRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist"));
    }

    public Wallet findByOwner(String owner) {
        Optional<User> user = userRepository.findByUsername(owner);
        if(user.isPresent())
            return walletRepository.findByOwnerId(user.get().getId());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public Wallet findByOwner(long ownerId) {
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isPresent())
            return walletRepository.findByOwnerId(ownerId);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public Wallet update(Wallet wallet) {
        Optional<Wallet> existing = walletRepository.findById(wallet.getId());
        if(existing.isPresent())
            return walletRepository.save(wallet);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
    }

    public void delete(long id) {
        Optional<Wallet> existing = walletRepository.findById(id);
        if(existing.isPresent())
            walletRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
        }
    }

    public void activate(long walletId) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId);
        if(walletOptional.isPresent()){
            Wallet wallet = walletOptional.get();
            wallet.setEnabled(true);
            walletRepository.save(wallet);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
    }

    public void deactivate(long walletId) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId);
        if(walletOptional.isPresent()){
            Wallet wallet = walletOptional.get();
            wallet.setEnabled(false);
            walletRepository.save(wallet);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
    }
}
