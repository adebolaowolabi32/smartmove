package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.model.request.Transfer;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.WalletRepository;
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
    WalletRepository walletRepository;

    @Autowired
    UserRepository userRepository;

    public Wallet save(Wallet wallet) {
        long id = wallet.getId();
        boolean exists = walletRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Wallet already exists");
        return walletRepository.save(wallet);
    }

    public Wallet findById(long id) {
        return walletRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist"));
    }

    public Wallet findByOwner(String owner) {
        Optional<User> user = userRepository.findByUsername(owner);
        if(user.isPresent())
            return walletRepository.findByOwner(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist"));;
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


    public void transfer(Transfer transfer) {
        double amount = transfer.getAmount();
        Optional<Wallet> fromOptional = walletRepository.findById(transfer.getFrom());
        if(fromOptional.isPresent()){
            Optional<Wallet> toOptional = walletRepository.findById(transfer.getTo());
            if(toOptional.isPresent()) {
                Wallet from = fromOptional.get();
                Wallet to = toOptional.get();
                double fromBalance = from.getBalance();
                double toBalance = to.getBalance();
                fromBalance = fromBalance - amount;
                toBalance = toBalance + amount;
                from.setBalance(fromBalance);
                to.setBalance(toBalance);
                walletRepository.save(from);
                walletRepository.save(to);
            }
            else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient wallet does not exist");
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender wallet does not exist");
    }

    public void activate(long walletId) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId);
        if(walletOptional.isPresent()){
            Wallet wallet = walletOptional.get();
            wallet.setActive(true);
            walletRepository.save(wallet);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
    }

    public void deactivate(long walletId) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId);
        if(walletOptional.isPresent()){
            Wallet wallet = walletOptional.get();
            wallet.setActive(false);
            walletRepository.save(wallet);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
    }
}
