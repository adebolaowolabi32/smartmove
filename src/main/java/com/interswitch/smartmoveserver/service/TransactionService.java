package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.DeviceRepository;
import com.interswitch.smartmoveserver.repository.TransactionRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    public Iterable<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction does not exist"));
    }

    public Iterable<Transaction> find(Enum.TransactionType type) {
        return transactionRepository.findAllByType(type);
    }

    public Iterable<Transaction> findBySender(long sender) {
        Optional<User> user = userRepository.findById(sender);
        if(user.isPresent())
            return transactionRepository.findAllBySender(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender does not exist");
    }

    public Iterable<Transaction> findByRecipient(long recipient) {
        Optional<User> user = userRepository.findById(recipient);
        if(user.isPresent())
            return transactionRepository.findAllByRecipient(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient does not exist");
    }

    public Iterable<Transaction> findByDevice(long deviceId) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if(device.isPresent())
            return transactionRepository.findAllByDevice(device.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
    }

    public Iterable<Transaction> findByCardNumber(String cardNumber) {
        return transactionRepository.findAllByCardNumber(cardNumber);
    }
}
