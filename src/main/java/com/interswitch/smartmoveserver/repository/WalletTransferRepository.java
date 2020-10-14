package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Transfer;
import com.interswitch.smartmoveserver.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransferRepository extends CrudRepository<Transfer, Long> {
    Page<Transfer> findByWallet(Pageable pageable, Wallet wallet);

    Page<Transfer> findAll(Pageable pageable);
    Long countByWallet(Wallet wallet);
}