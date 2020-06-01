package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.model.WalletTransfer;
import com.interswitch.smartmoveserver.model.request.Transfer;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.WalletRepository;
import com.interswitch.smartmoveserver.repository.WalletTransferRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepository;

    @Autowired
    WalletTransferRepository transferRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PageUtil pageUtil;

    public Wallet save(Wallet wallet) {
        long id = wallet.getId();
        boolean exists = walletRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Wallet already exists");
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
            return this.findByOwner(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public Wallet findByOwner(User user) {
        return walletRepository.findByOwner(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist"));
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

    public Page<WalletTransfer> findAllTransfers(Principal principal, Long owner, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<Wallet> existing = walletRepository.findById(owner);
        if(existing.isPresent()){
            Wallet wallet = existing.get();
            return transferRepository.findByWallet(pageable, wallet);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
    }

    public Long countTransfers(Principal principal, User owner){
        Optional<Wallet> existing = walletRepository.findByOwner(owner);
        if(existing.isPresent()){
            Wallet wallet = existing.get();
            return transferRepository.countByWallet(wallet);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet does not exist");
    }

    public void transfer(Transfer transfer, User authenticatedUser) {
        double amount = transfer.getAmount();
        Optional<Wallet> fromOptional = walletRepository.findById(authenticatedUser.getId());
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
