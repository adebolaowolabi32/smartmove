package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.TripReference;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.TripReferenceRepository;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author adebola.owolabi
 */
@Service
public class TripReferenceService {

    @Autowired
    TripReferenceRepository tripRefRepository;

    @Autowired
    UserService userService;

    public Iterable<TripReference> findAll() {
        return tripRefRepository.findAll();
    }


    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public TripReference save(TripReference tripReference, String principal) {
        User owner = userService.findByUsername(principal);
        tripReference.setPrefix(tripReference.getPrefix().toUpperCase());
        tripReference.setOwner(owner);
        return tripRefRepository.save(tripReference);
    }

    public TripReference findByOwner(String principal) {
        User owner = userService.findByUsername(principal);
        if (owner != null) return tripRefRepository.findByOwner(owner);
        return null;
    }

    public TripReference update(TripReference tripReference, String principal) {
        User owner = userService.findByUsername(principal);
        TripReference tripReference1 = tripRefRepository.findByOwner(owner);
        if (tripReference1 != null) {
            tripReference.setId(tripReference1.getId());
            tripReference.setOwner(tripReference1.getOwner());
            tripReference.setPrefix(tripReference.getPrefix().toUpperCase());
            return tripRefRepository.save(tripReference);
        }
        return null;
    }

    public void delete(String principal) {
        User owner = userService.findByUsername(principal);
        TripReference existing = tripRefRepository.findByOwner(owner);
        if (existing != null)
            tripRefRepository.deleteByOwner(owner);
    }
}
