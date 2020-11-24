package com.interswitch.smartmoveserver.repository;


import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.UserSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsRepository extends CrudRepository<UserSettings, Long> {
    UserSettings findByOwner(User owner);
}
