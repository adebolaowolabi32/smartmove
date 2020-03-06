package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
    Optional<Card> findByPan(String pan);
    Optional<Card> findByOwner(User owner);
    Page<Card> findAll(Pageable pageable);
    List<Card> findAll();
}
