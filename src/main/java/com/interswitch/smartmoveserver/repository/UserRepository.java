package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByMobileNo(String mobile);
    Optional<User> findByEmail(String email);
    Page<User> findAllByRole(Pageable pageable, Enum.Role role);
    List<User> findAllByRole(Enum.Role role);
    List<User> findAllByOwner(User owner);
    List<User> findAllByRoleAndOwner(Enum.Role type, User owner);
    Long countByRole(Enum.Role role);
    Long countByRoleAndOwner(Enum.Role role, User user);
    Page<User> findAll(Pageable pageable);
    Page<User> findAllByRoleAndOwner(Pageable pageable, Enum.Role role, User owner);
    List<User> findAll();
    boolean existsByUsername(String username);
}