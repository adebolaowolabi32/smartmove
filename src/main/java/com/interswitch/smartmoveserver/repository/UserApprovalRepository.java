package com.interswitch.smartmoveserver.repository;


import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.UserApproval;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserApprovalRepository extends CrudRepository<UserApproval, Long> {
    List<UserApproval> findAllByOwner(User owner);

    List<UserApproval> findAll();
}
