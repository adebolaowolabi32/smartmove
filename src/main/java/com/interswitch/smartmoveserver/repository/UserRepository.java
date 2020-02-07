package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User findByMobileNumber(String name);
    User findByEmailAddress(String email);
    List<User> findAllByType(User.UserType type);
    List<User> findAllByTypeAndParent(User.UserType type, String parent);


}