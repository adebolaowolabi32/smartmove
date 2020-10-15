package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Ticket;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Page<Ticket> findAllByOperator(Pageable pageable, User operator);

    Ticket findByReferenceNo(String referenceNo);
}
