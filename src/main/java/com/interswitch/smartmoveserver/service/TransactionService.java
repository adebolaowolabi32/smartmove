package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
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
import java.util.UUID;

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
        transaction.setTransactionId(UUID.randomUUID().toString());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAll() {
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

    public List<Transaction> findByOperator(String operatorId) {
        return transactionRepository.findAllByOperatorId(operatorId);
    }

    public Long countAll(){
        return transactionRepository.count();
    }

}
