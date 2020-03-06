package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.DeviceRepository;
import com.interswitch.smartmoveserver.repository.TransactionRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeviceRepository deviceRepository;

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Page<Transaction> findAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return transactionRepository.findAll(pageable);
    }

    public Transaction findById(long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction does not exist"));
    }

    public List<Transaction> find(Enum.TransactionType type) {
        return transactionRepository.findAllByType(type);
    }

    public List<Transaction> findBySender(long sender) {
        Optional<User> user = userRepository.findById(sender);
        if(user.isPresent())
            return transactionRepository.findAllBySender(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender does not exist");
    }

    public List<Transaction> findByRecipient(long recipient) {
        Optional<User> user = userRepository.findById(recipient);
        if(user.isPresent())
            return transactionRepository.findAllByRecipient(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient does not exist");
    }

    public List<Transaction> findByDevice(long deviceId) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if(device.isPresent())
            return transactionRepository.findAllByDevice(device.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
    }

    public List<Transaction> findByCardNumber(String cardNumber) {
        return transactionRepository.findAllByCard(new Card());
    }

    public Long countAll(){
        return transactionRepository.count();
    }

}
