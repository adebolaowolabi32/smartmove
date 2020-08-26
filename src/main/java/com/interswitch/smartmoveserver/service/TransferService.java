package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.WalletTransfer;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.WalletTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


/**
 * @author adebola.owolabi
 */
@Service
public class TransferService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private WalletTransferRepository transferRepository;

    public WalletTransfer save(WalletTransfer transfer) {
        return transferRepository.save(transfer);
    }

    public Page<WalletTransfer> findAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return transferRepository.findAll(pageable);
    }

    public WalletTransfer findById(long id) {
        return transferRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer does not exist"));
    }

    public Long countAll() {
        return transferRepository.count();
    }

}
