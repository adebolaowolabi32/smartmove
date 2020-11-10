package com.interswitch.smartmoveserver.repository;


import com.interswitch.smartmoveserver.model.TicketReference;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketReferenceRepository extends CrudRepository<TicketReference, Long> {
    TicketReference findByOwner(User owner);
    void deleteByOwner(User owner);

}
