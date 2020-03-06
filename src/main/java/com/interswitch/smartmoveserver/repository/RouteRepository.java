package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  RouteRepository extends CrudRepository<Route, Long> {
    List<Route> findAllByType(Enum.TransportMode type);
    List<Route> findAllByOwner(User owner);
    Page<Route> findAllByType(Pageable pageable, Enum.TransportMode type);
    Page<Route> findAllByOwner(Pageable pageable, User owner);
    Long countByOwner(User owner);
    Page<Route> findAll(Pageable pageable);
    List<Route> findAll();
}