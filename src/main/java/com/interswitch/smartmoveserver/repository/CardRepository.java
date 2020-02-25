package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
    Optional<Card> findByPan(String pan);
}
