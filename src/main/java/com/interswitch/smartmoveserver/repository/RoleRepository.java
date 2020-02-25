package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
