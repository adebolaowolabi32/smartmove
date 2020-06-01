package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Blacklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/*
 * Created by adebola.owolabi on 5/21/2020
 */
@Repository
public interface BlacklistRepository  extends CrudRepository<Blacklist, Long> {
    Page<Blacklist> findAll(Pageable pageable);

}
