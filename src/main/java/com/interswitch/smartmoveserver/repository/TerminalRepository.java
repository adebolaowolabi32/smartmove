package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminalRepository extends CrudRepository<Terminal, Long>  {
    List<Terminal> findAllByType(Enum.TransportMode type);
    List<Terminal> findAllByOwner(User owner);
    List<Terminal> findAllByType(Pageable pageable, Enum.TransportMode type);
    List<Terminal> findAllByOwner(Pageable pageable, User owner);
    Long countByOwner(User owner);
    Page<Terminal> findAll(Pageable pageable);
    List<Terminal> findAll();
}
