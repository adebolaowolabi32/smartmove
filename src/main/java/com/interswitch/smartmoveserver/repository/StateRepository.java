package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<State, Long> {
    boolean existsByNameIgnoreCase(String name);
    State findById(long id);
    List<State> findAll();
    State findByName(String name);
}
