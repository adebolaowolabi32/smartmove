package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.view.RefundTicket;
import com.interswitch.smartmoveserver.repository.TicketRefundRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 7/27/2020
 */
@Service
public class TicketRefundService {
    @Autowired
    PageUtil pageUtil;

    @Autowired
    private TicketRefundRepository refundRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ManifestService manifestService;
    @Autowired
    private UserService userService;

    public RefundTicket refundTicket(String username, RefundTicket refundTicket) {
        Ticket ticket = ticketService.findByReferenceNo(refundTicket.getReferenceNo().trim());
        if (ticket != null) refundTicket.setTicket(ticket);
        else refundTicket.setInvalid(true);
        return refundTicket;
    }

    public TicketRefund confirmRefund(String username, RefundTicket refundTicket) {
        Ticket ticket = refundTicket.getTicket();
        TicketRefund refund = new TicketRefund();
        refund.setTicket(ticket);
        refund.setOperator(ticket.getOperator());
        refund.setReason(refundTicket.getReason());
        refund.setRefundDateTime(LocalDateTime.now());
        refund.setOperator(userService.findByUsername(username));
        Ticket ticket1 = ticketService.findById(ticket.getId());
        ticket1.setRefunded(true);
        ticketService.save(ticket1);
        Manifest manifest = manifestService.findByScheduleIdAndName(ticket.getSchedule().getId(), ticket.getPassengerName());
        manifestService.delete(manifest.getId());
        return this.save(refund);
    }

    public TicketRefund save(TicketRefund refund) {
        return refundRepository.save(refund);
    }

    public TicketRefund findById(long id) {
        return refundRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Refund not found"));
    }

    public PageView<TicketRefund> findAllByOperator(int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);
        Page<TicketRefund> pages = refundRepository.findAllByOperator(pageable, user);
        return new PageView<>(pages.getTotalElements(), pages.getContent());
    }
}
