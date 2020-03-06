package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class ConfigService {
    @Autowired
    private ConfigRepository configRepository;

    public List<Config> getAll() {
        return configRepository.findAll();
    }

    public Page<Config> findAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return configRepository.findAll(pageable);
    }

    public Config save(Config config) {
        Enum.ConfigList configName = config.getName();
        boolean exists = configRepository.existsByName(configName);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Configuration already exists");
        return configRepository.save(config);
    }

    public Config findById(long id) {
        return configRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Configuration does not exist"));
    }

    public Config findByName(Enum.ConfigList key) {
        return configRepository.findByName(key).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Configuration does not exist"));
    }

    public Config update(Config config) {
        Optional<Config> existing = configRepository.findById(config.getId());
        if(existing.isPresent())
            return configRepository.save(config);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Configuration does not exist");
    }

    public void delete(long id) {
        Optional<Config> existing = configRepository.findById(id);
        if(existing.isPresent())
            configRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Configuration does not exist");
        }
    }

    public Long countAll(){
        return configRepository.count();
    }
}
