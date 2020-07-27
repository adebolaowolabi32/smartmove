package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {
    List<Device> findAllByType(Enum.DeviceType type);
    List<Device> findAllByOwner(User owner);
    Page<Device> findAllByType(Pageable pageable, Enum.DeviceType type);
    Page<Device> findAllByOwner(Pageable pageable, User owner);

    Page<Device> findAllByTypeAndOwner(Pageable pageable, Enum.DeviceType type, User owner);
    Page<Device> findAll(Pageable pageable);
    List<Device> findAll();
    Long countByOwner(User owner);
    Long countByType(Enum.DeviceType type);
    Long countByTypeAndOwner(Enum.DeviceType type, User owner);
}
