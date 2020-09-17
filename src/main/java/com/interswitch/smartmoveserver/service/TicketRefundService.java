package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Ticket;
import com.interswitch.smartmoveserver.model.TicketRefund;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.view.RefundTicket;
import com.interswitch.smartmoveserver.repository.TicketRefundRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

/*
 * Created by adebola.owolabi on 7/27/2020
 */
@Service
public class TicketRefundService {
    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    PageUtil pageUtil;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketRefundRepository refundRepository;
    @Autowired
    private UserRepository userRepository;

    public RefundTicket refundTicket(String username, RefundTicket refundTicket) {
        Ticket ticket = ticketService.findByReferenceNo(refundTicket.getReferenceNo());
        if (ticket != null) refundTicket.setTicket(ticket);
        else refundTicket.setInvalid(true);
        return refundTicket;
    }

    public TicketRefund confirmRefund(String username, RefundTicket refundTicket) {
        TicketRefund refund = new TicketRefund();
        refund.setOperator(refundTicket.getTicket().getOperator());
        refund.setTicket(refundTicket.getTicket());
        refund.setReason(refundTicket.getReason());
        refund.setRefundDateTime(LocalDateTime.now());
        return this.save(refund);
    }

    public TicketRefund save(TicketRefund refund) {
        return refundRepository.save(refund);
    }

    public Page<TicketRefund> findAllByOperator(Principal principal, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent())
            return refundRepository.findAllByOperator(pageable, user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Refund Owner not found");
    }
}
