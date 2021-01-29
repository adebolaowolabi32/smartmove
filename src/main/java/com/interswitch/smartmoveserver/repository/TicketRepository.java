package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Ticket;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Page<Ticket> findAllByOperator(Pageable pageable, User operator);

    Page<Ticket> findAll(Pageable pageable);

    List<Ticket> findAllByOperator(User operator);

    List<Ticket> findAll();
    Ticket findByReferenceNo(String referenceNo);
}
