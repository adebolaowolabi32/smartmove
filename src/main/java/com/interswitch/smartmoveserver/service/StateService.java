package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.State;
import com.interswitch.smartmoveserver.repository.StateRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class StateService {
    @Autowired
    StateRepository stateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<State> findAll() {
        return stateRepository.findAll();
    }

    public State save(State state) {
        long id = state.getId();
        boolean exists = stateRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "State already exists");
        return stateRepository.save(state);
    }

    public State findById(long id) {
        return stateRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "State does not exist"));
    }

    public State findByName(String name) {
        return stateRepository.findByName(name);
    }

    public State update(State state) {
        Optional<State> existing = stateRepository.findById(state.getId());
        if(existing.isPresent())
            return stateRepository.save(state);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "State does not exist");
    }

    public void delete(long id) {
        Optional<State> existing = stateRepository.findById(id);
        if(existing.isPresent())
            stateRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "State does not exist");
        }
    }

    public List<String> findAllCountries() {
        List<String> countries = new ArrayList<>();
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("en", countryCode);
            countries.add(obj.getDisplayCountry());
        }
        return countries;
    }
}
