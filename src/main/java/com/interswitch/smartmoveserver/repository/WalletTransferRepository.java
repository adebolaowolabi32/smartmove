package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Wallet;
import com.interswitch.smartmoveserver.model.WalletTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/*
 * Created by adebola.owolabi on 5/19/2020
 */
@Repository
public interface WalletTransferRepository extends CrudRepository<WalletTransfer, Long> {
    Page<WalletTransfer> findByWallet(Pageable pageable, Wallet wallet);
    Long countByWallet(Wallet wallet);
}