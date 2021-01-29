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
    List<Route> findAllByMode(Enum.TransportMode mode);
    List<Route> findAllByOwner(User owner);

    Page<Route> findAllByMode(Pageable pageable, Enum.TransportMode mode);
    Page<Route> findAllByOwner(Pageable pageable, User owner);
    Long countByOwner(User owner);
    Page<Route> findAll(Pageable pageable);
    List<Route> findAll();

    boolean existsByName(String name);
}