package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Transfer;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransferRepository extends CrudRepository<Transfer, Long> {

    List<Transfer> findAllByOwner(User owner);

    Page<Transfer> findAllByOwner(Pageable pageable, User owner);

    Page<Transfer> findAll(Pageable pageable);

    Long countByOwner(User owner);
}