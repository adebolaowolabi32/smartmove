package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.view.TicketTillView;
import com.interswitch.smartmoveserver.repository.TicketTillRepository;
import com.interswitch.smartmoveserver.repository.TicketTillSummaryRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.DateUtil;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class TicketTillService {

    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    PageUtil pageUtil;
    @Autowired
    private TicketTillRepository ticketTillRepository;
    @Autowired
    private TicketTillSummaryRepository ticketTillSummaryRepository;
    @Autowired
    private UserRepository userRepository;

    @Async
    public void pushDataToTicketTill(Iterable<Ticket> iterableTicket) {
        iterableTicket.forEach(
                ticketsIt -> {
                    User user = ticketsIt.getOperator();

                    TicketTill ticketTill = TicketTill.builder()
                            .tillOperatorName(user.getFirstName().concat(" " + user.getLastName()))
                            .tillOperatorUsername(user.getUsername())
                            .tillOperatorId(user.getId())
                            .ticketIssuanceDate(LocalDate.now())
                            .ticketIssuanceTime(LocalTime.now())
                            .ticketId(ticketsIt.getId())
                            .totalAmount(ticketsIt.getFare())
                            .tillOperatorOwnerId(user.getOwner() != null ? user.getOwner().getId() : 0)
                            .approved(false).closed(false).build();

                    ticketTillRepository.save(ticketTill);
                });

    }

    public TicketTillView findCurrentUserTicketTillStatus(User user) {

        if (user != null && user.getId() > 0) {
            TicketTillView ticketTillView = ticketTillRepository
                    .findCurrentTicketTillStatusByDateAndTillOperatorId(DateUtil.getTodayDate(), user.getId());
            return ticketTillView;
        }
        return null;
    }

    public void closeTicketTill(TicketTillView ticketTill) {
        if (ticketTill != null) {
            //update ticket till closed=true
            ticketTillRepository.updateTicketTillStatusByTillOperatorIdAndIssuanceDate(ticketTill.getTicketIssuanceDate(), ticketTill.getTillOperatorId());
            //submit till to ticketTillSummary
            ticketTillSummaryRepository.save(TicketTillSummary.builder()
                    .tillStartTime(ticketTill.getTillStartTime())
                    .tillEndTime(ticketTill.getTillClosureTime())
                    .tillOperatorId(ticketTill.getTillOperatorId())
                    .tillOperatorName(ticketTill.getTillOperatorName())
                    .totalSoldTickets(ticketTill.getTotalNumberOfTickets())
                    .totalSoldAmount(ticketTill.getTotalSoldTicketValue())
                    .tillOperatorOwner(ticketTill.getTillOperatorOwnerId())
                    .approver(null)
                    .approved(false)
                    .date(LocalDate.now())
                    .build());

            //set user's till status = CLOSE
            Optional<User> userOptionalWrapper = userRepository.findById(ticketTill.getTillOperatorId());
            User user = userOptionalWrapper.get();
            user.setTillStatus(Enum.TicketTillStatus.CLOSE);
            userRepository.save(user);
        }

    }

    public void approveTicketTill(User user, long ticketTillSummaryId) {
        Optional<TicketTillSummary> ticketTillSummaryOptional = ticketTillSummaryRepository.findById(ticketTillSummaryId);
        if (ticketTillSummaryOptional.isPresent()) {
            TicketTillSummary ticketTillSummary = ticketTillSummaryOptional.get();
            ticketTillSummary.setApprover(user);
            ticketTillSummary.setApproved(true);
            ticketTillSummaryRepository.save(ticketTillSummary);
            //set till operator/user's till status open
            Optional<User> userOptionalWrapper = userRepository.findById(ticketTillSummary.getTillOperatorId());
            User tillOperator = userOptionalWrapper.get();
            tillOperator.setTillStatus(Enum.TicketTillStatus.OPEN);
            userRepository.save(user);
        }


    }

    public PageView<TicketTillSummary> findUnApprovedTicketTillSummary(long tillOperatorId, long tillOperatorOwner, boolean approved, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Set<Long> ids = new HashSet<>();
        ids.addAll(Arrays.asList(tillOperatorId, tillOperatorOwner));
        Page<TicketTillSummary> pages = ticketTillSummaryRepository.findByTillOperatorOwnerInAndApproved(pageable, ids, approved);
        return new PageView<>(pages.getTotalElements(), pages.getContent());
    }

    public List<TicketTillView> findAggregatedTicketTillByIssuanceDateAndStatus(String date, boolean closed) {
        return ticketTillRepository.findAggregatedTicketTillByIssuanceDateAndStatus(date, closed);
    }

}
