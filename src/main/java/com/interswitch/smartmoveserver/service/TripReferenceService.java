package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.TripReference;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.TripReferenceRepository;
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

    public TripReference save(TripReference tripReference, String principal) {
        User owner = userService.findByUsername(principal);
        tripReference.setOwner(owner);
        return tripRefRepository.save(tripReference);
    }

    public TripReference findByOwner(String principal) {
        User owner = userService.findByUsername(principal);
        return tripRefRepository.findByOwner(owner);
    }

    public TripReference update(TripReference tripReference, String principal) {
        User owner = userService.findByUsername(principal);
        TripReference tripReference1 = tripRefRepository.findByOwner(owner);
        if (tripReference1 != null)
        {
            tripReference.setId(tripReference1.getId());
            tripReference.setOwner(tripReference1.getOwner());
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
