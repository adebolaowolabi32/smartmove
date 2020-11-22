package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.UserSettings;
import com.interswitch.smartmoveserver.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author adebola.owolabi
 */
@Service
public class UserSettingsService {

    @Autowired
    UserSettingsRepository userSettingsRepository;

    @Autowired
    UserService userService;

    public Iterable<UserSettings> findAll() {
        return userSettingsRepository.findAll();
    }

    public UserSettings save(UserSettings userSettings, String principal) {
        User owner = userService.findByUsername(principal);
        userSettings.setOwner(owner);
        return userSettingsRepository.save(userSettings);
    }

    public UserSettings findByOwner(String principal) {
        User owner = userService.findByUsername(principal);
        return userSettingsRepository.findByOwner(owner);
    }

    public UserSettings update(UserSettings userSettings, String principal) {
        User owner = userService.findByUsername(principal);
        UserSettings userSettings1 = userSettingsRepository.findByOwner(owner);
        if (userSettings1 != null) {
            userSettings.setId(userSettings1.getId());
            userSettings.setOwner(userSettings1.getOwner());
            return userSettingsRepository.save(userSettings);
        }
        return null;
    }
}
