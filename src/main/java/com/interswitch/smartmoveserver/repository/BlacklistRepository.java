package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Blacklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository  extends CrudRepository<Blacklist, Long> {
    Page<Blacklist> findAll(Pageable pageable);

}
