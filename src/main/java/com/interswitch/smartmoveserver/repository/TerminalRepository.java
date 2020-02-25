package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalRepository extends CrudRepository<Terminal, Long>  {
    Iterable<Terminal> findAllByType(Enum.TransportMode type);
    Iterable<Terminal> findAllByOwner(User owner);
}
