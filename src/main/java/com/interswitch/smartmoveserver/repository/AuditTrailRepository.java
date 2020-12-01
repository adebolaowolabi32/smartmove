package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.AuditTrail;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuditTrailRepository extends CrudRepository<AuditTrail, Long> {

    List<AuditTrail> findAll();

    Page<AuditTrail> findAll(Pageable pageable);

    AuditTrail findById(long feeConfigId);

    Page<AuditTrail> findAllByOwner(Pageable pageable, User owner);

    Long countByOwner(User owner);


}
