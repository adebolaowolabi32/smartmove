package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findByMobileNo(String mobile);
    Optional<User> findByEmail(String email);
    Iterable<User> findAllByType(Enum.UserType type);
    Iterable<User> findAllByParent(User parent);
    Iterable<User> findAllByTypeAndParentId(Enum.UserType type, long parent);
}