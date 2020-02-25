package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
}