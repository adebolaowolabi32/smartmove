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
    List<User> findAllByParent(User parent);
    List<User> findAllByRoleAndParent(Enum.Role type, User parent);
    Long countByRole(Enum.Role role);
    Long countByRoleAndParent(Enum.Role role, User user);
    Page<User> findAll(Pageable pageable);
    List<User> findAll();
}