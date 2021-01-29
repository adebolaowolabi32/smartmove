package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.TripReference;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripReferenceRepository extends CrudRepository<TripReference, Long> {
    TripReference findByOwner(User owner);

    void deleteByOwner(User owner);

}
