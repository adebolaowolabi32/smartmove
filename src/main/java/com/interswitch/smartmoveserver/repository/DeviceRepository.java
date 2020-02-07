package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends CrudRepository<Device, String> {
}
