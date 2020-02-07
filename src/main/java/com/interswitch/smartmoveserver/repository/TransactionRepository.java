package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {
}
