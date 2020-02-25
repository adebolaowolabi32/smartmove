package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Iterable<Transaction> findAllByType(Enum.TransactionType type);
    Iterable<Transaction> findAllBySender(User sender);
    Iterable<Transaction> findAllByRecipient(User recipient);
    Iterable<Transaction> findAllByDevice(Device device);
    Iterable<Transaction> findAllByCardNumber(String cardNumber);
}
