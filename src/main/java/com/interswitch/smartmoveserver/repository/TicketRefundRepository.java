package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.TicketRefund;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Created by adebola.owolabi on 7/27/2020
 */
@Repository
public interface TicketRefundRepository extends CrudRepository<TicketRefund, Long> {
    List<TicketRefund> findAllByOperator(User operator);
    Page<TicketRefund> findAllByOperator(Pageable pageable, User operator);
}
