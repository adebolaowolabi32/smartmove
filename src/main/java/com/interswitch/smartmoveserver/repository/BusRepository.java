package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Bus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends CrudRepository<Bus, String> {
}
