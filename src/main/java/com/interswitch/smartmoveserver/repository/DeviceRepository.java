package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {
    Iterable<Device> findAllByType(Enum.DeviceType type);
    Iterable<Device> findAllByOwner(User owner);
}
