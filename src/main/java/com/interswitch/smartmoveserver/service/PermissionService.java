package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Permission;
import com.interswitch.smartmoveserver.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PermissionService {
    @Autowired
    PermissionRepository permissionRepository;

    public Iterable<Permission> getAll() {
        return permissionRepository.findAll();
    }

    public Permission save(Permission permission) {
        long id = permission.getId();
        boolean exists = permissionRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Permission already exists");
        return permissionRepository.save(permission);
    }

    public Permission findById(long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission does not exist"));
    }

    public Permission update(Permission permission) {
        Optional<Permission> existing = permissionRepository.findById(permission.getId());
        if(existing.isPresent())
            return permissionRepository.save(permission);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission does not exist");
    }

    public void delete(long id) {
        Optional<Permission> existing = permissionRepository.findById(id);
        if(existing.isPresent())
            permissionRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission does not exist");
        }
    }
}
