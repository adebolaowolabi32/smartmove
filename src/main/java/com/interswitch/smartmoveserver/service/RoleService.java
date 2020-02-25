package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Role;
import com.interswitch.smartmoveserver.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public Iterable<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role save(Role role) {
        long id = role.getId();
        boolean exists = roleRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists");
        return roleRepository.save(role);
    }

    public Role findById(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role does not exist"));
    }

    public Role update(Role role) {
        Optional<Role> existing = roleRepository.findById(role.getId());
        if(existing.isPresent())
            return roleRepository.save(role);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role does not exist");
    }

    public void delete(long id) {
        Optional<Role> existing = roleRepository.findById(id);
        if(existing.isPresent())
            roleRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role does not exist");
        }
    }
}
