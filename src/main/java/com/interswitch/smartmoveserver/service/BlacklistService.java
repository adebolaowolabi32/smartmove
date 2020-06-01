package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Blacklist;
import com.interswitch.smartmoveserver.repository.BlacklistRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/*
 * Created by adebola.owolabi on 5/21/2020
 */
@Service
public class BlacklistService {
    @Autowired
    BlacklistRepository blacklistRepository;

    @Autowired
    UserRepository userRepository;

    public Page<Blacklist> findAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return blacklistRepository.findAll(pageable);
    }

    public Blacklist add(Blacklist blacklist) {
        long id = blacklist.getId();
        boolean exists = blacklistRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Item already exists");
        return blacklistRepository.save(blacklist);
    }

    public Blacklist findById(long id) {
        return blacklistRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blacklist does not exist"));
    }

    public Blacklist update(Blacklist blacklist) {
        Optional<Blacklist> existing = blacklistRepository.findById(blacklist.getId());
        if(existing.isPresent())
            return blacklistRepository.save(blacklist);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blacklist does not exist");
    }

    public void remove(long id) {
        Optional<Blacklist> existing = blacklistRepository.findById(id);
        if(existing.isPresent())
            blacklistRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blacklist does not exist");
        }
    }
}
