package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.TerminalRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class TerminalService {
    @Autowired
    TerminalRepository terminalRepository;

    @Autowired
    UserRepository userRepository;

    public Iterable<Terminal> getAll() {
        return terminalRepository.findAll();
    }

    public Terminal save(Terminal terminal) {
        long id = terminal.getId();
        boolean exists = terminalRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Terminal already exists");
        return terminalRepository.save(terminal);
    }

    public Terminal findById(long id) {
        return terminalRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist"));
    }

    public Iterable<Terminal> find(Enum.TransportMode type) {
        return terminalRepository.findAllByType(type);
    }

    public Iterable<Terminal> findByOwner(long owner) {
        Optional<User> user = userRepository.findById(owner);
        if(user.isPresent())
            return terminalRepository.findAllByOwner(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public Terminal update(Terminal terminal) {
        Optional<Terminal> existing = terminalRepository.findById(terminal.getId());
        if(existing.isPresent())
            return terminalRepository.save(terminal);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
    }

    public void delete(long id) {
        Optional<Terminal> existing = terminalRepository.findById(id);
        if(existing.isPresent())
            terminalRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
        }
    }

    public void activate(long terminalId) {
        Optional<Terminal> terminalOptional = terminalRepository.findById(terminalId);
        if(terminalOptional.isPresent()){
            Terminal terminal = terminalOptional.get();
            terminal.setActive(true);
            terminalRepository.save(terminal);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
    }

    public void deactivate(long terminalId) {
        Optional<Terminal> terminalOptional = terminalRepository.findById(terminalId);
        if(terminalOptional.isPresent()){
            Terminal terminal = terminalOptional.get();
            terminal.setActive(false);
            terminalRepository.save(terminal);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
    }
}
