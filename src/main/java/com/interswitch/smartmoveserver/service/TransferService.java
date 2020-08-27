package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Transfer;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.repository.WalletTransferRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;


/**
 * @author adebola.owolabi
 */
@Service
public class TransferService {

    @Autowired
    private WalletTransferRepository transferRepository;

    @Autowired
    PageUtil pageUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;

    public Transfer save(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    public Page<Transfer> findAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return transferRepository.findAll(pageable);
    }

    public Transfer findById(long id) {
        return transferRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer does not exist"));
    }

    public Long countAll() {
        return transferRepository.count();
    }

   /* public Page<Transfer> findAllTransfers(Principal principal, Long owner, int page, int size) {
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
    }*/

    public String transfer(Principal principal, Transfer transfer) {
        User user = userService.findByUsername(principal.getName());

        double amount = transfer.getAmount();
        Wallet from = walletService.findByOwner(principal.getName());
        Wallet to = walletService.findByOwner(Long.parseLong(transfer.getRecipient()));
        double fromBalance = from.getBalance();
        double toBalance = to.getBalance();
        if (fromBalance >= amount) {
            fromBalance = fromBalance - amount;
            toBalance = toBalance + amount;
            from.setBalance(fromBalance);
            to.setBalance(toBalance);
            walletService.save(from);
            walletService.save(to);
            Transfer transfer1 = new Transfer();
            transfer1.setAmount(amount);
            transfer1.setRecipient(to.getOwner().getUsername());
            transfer1.setWallet(from);
            transfer1.setTransferDateTime(LocalDateTime.now());
            save(transfer1);
        }
        return "Insufficient funds";
        //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Insufficient funds");
    }

}
