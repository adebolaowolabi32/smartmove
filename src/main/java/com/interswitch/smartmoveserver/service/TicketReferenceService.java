package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.TicketReference;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.TicketReferenceRepository;
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

    public TicketReference save(TicketReference ticketReference, String principal) {
        User owner = userService.findByUsername(principal);
        ticketReference.setOwner(owner);
        return ticketReferenceRepository.save(ticketReference);
    }

    public TicketReference findByOwner(String principal) {
        User owner = userService.findByUsername(principal);
        return ticketReferenceRepository.findByOwner(owner);
    }

    public TicketReference update(TicketReference ticketReference, String principal) {
        User owner = userService.findByUsername(principal);
        TicketReference tripReference1 = ticketReferenceRepository.findByOwner(owner);
        if (tripReference1 != null)
        {
            ticketReference.setId(tripReference1.getId());
            ticketReference.setOwner(tripReference1.getOwner());
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
