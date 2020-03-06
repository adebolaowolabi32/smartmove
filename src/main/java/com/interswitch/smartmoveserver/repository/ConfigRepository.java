package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.model.Enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepository extends CrudRepository<Config, Long> {
    Optional<Config> findByName(Enum.ConfigList name);
    boolean existsByName(Enum.ConfigList name);
    Page<Config> findAll(Pageable pageable);
    List<Config> findAll();
}
