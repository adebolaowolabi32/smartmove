package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Config;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends CrudRepository<Config, String> {
}
