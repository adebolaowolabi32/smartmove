package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.TicketReference;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.TicketReferenceRepository;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author adebola.owolabi
 */
@Service
public class TicketReferenceService {

    @Autowired
    TicketReferenceRepository ticketReferenceRepository;

    @Autowired
    UserService userService;

    public Iterable<TicketReference> findAll() {
        return ticketReferenceRepository.findAll();
    }


    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public TicketReference save(TicketReference ticketReference, String principal) {
        User owner = userService.findByUsername(principal);
        ticketReference.setOwner(owner);
        ticketReference.setPrefix(ticketReference.getPrefix().toUpperCase());
        return ticketReferenceRepository.save(ticketReference);
    }


    public TicketReference findByOwner(String principal) {
        User owner = userService.findByUsername(principal);
        return ticketReferenceRepository.findByOwner(owner);
    }

    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public TicketReference update(TicketReference ticketReference, String principal) {
        User owner = userService.findByUsername(principal);
        TicketReference ticketReference1 = ticketReferenceRepository.findByOwner(owner);
        if (ticketReference1 != null) {
            ticketReference.setId(ticketReference1.getId());
            ticketReference.setOwner(ticketReference1.getOwner());
            ticketReference.setPrefix(ticketReference.getPrefix().toUpperCase());
            return ticketReferenceRepository.save(ticketReference);
        }
        return null;
    }

    public void delete(String principal) {
        User owner = userService.findByUsername(principal);
        TicketReference existing = ticketReferenceRepository.findByOwner(owner);
        if (existing != null)
            ticketReferenceRepository.deleteByOwner(owner);
    }
}
