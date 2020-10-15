package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Ticket;
import com.interswitch.smartmoveserver.model.TicketTillSummary;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TicketTillSummaryRepository extends CrudRepository<TicketTillSummary, Long> {

    List<TicketTillSummary> findByTillOperatorId(long tillOperatorId);

    Page<TicketTillSummary> findAll(Pageable pageable);

    List<TicketTillSummary> findAll();

    Page<TicketTillSummary> findByTillOperatorOwnerInAndApproved(Pageable pageable, Set<Long> tillOperator, boolean approved);
}
