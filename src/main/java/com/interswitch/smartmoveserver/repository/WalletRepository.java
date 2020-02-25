package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Optional<Wallet> findByOwner(User owner);
}
