package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAllByType(Enum.TransactionType type);
    List<Transaction> findAllByOperatorId(String operatorId);

    List<Transaction> findAllByOwner(User owner);

    Page<Transaction> findAllByOwner(Pageable pageable, User owner);
    Page<Transaction> findAll(Pageable pageable);
    List<Transaction> findAll();

    Long countByOwner(User owner);
}
