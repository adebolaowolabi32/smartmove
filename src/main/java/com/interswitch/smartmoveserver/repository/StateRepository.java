package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<State, Long> {
    List<State> findAll();
    boolean existsByNameIgnoreCase(String name);
    State findByName(String name);
}
