package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Wallet findByOwnerId(long ownerId);
}
