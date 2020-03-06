package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findAllByType(Enum.TransactionType type);
    List<Transaction> findAllBySender(User sender);
    List<Transaction> findAllByRecipient(User recipient);
    List<Transaction> findAllByDevice(Device device);
    List<Transaction> findAllByCard(Card card);
    Page<Transaction> findAll(Pageable pageable);
    List<Transaction> findAll();
}
