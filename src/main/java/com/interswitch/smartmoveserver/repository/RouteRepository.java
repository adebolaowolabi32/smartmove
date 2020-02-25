package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {
    Iterable<Route> findAllByType(Enum.TransportMode type);
    Iterable<Route> findAllByOwner(User owner);
}