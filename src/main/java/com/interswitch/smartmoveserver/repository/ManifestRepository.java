package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Manifest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManifestRepository extends CrudRepository<Manifest, Long> {
    List<Manifest> findAll();

    Page<Manifest> findAll(Pageable pageable);

    Page<Manifest> findByTripId(Pageable pageable, long tripId);

    Page<Manifest> findByScheduleId(Pageable pageable, long scheduleId);

    Manifest findByTripIdAndName(long tripId, String name);

    Manifest findByScheduleIdAndName(long scheduleId, String name);

}
