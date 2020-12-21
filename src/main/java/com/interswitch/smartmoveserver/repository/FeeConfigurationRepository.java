package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.FeeConfiguration;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeConfigurationRepository extends CrudRepository<FeeConfiguration, Long> {

    List<FeeConfiguration> findAll();

    List<FeeConfiguration> findAllByOwner(User owner);

    Page<FeeConfiguration> findAll(Pageable pageable);

    List<FeeConfiguration> findByEnabledAndOperatorUsername(boolean enabled,String username);

    FeeConfiguration findById(long feeConfigId);

    Page<FeeConfiguration> findAllByOwner(Pageable pageable, User owner);

    Long countByOwner(User owner);

    boolean existsByFeeNameAndOperatorUsername(Enum.FeeName feeName, String operatorUsername);

}
