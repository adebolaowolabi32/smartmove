package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Manifest;
import com.interswitch.smartmoveserver.repository.ManifestRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ManifestService {

    @Autowired
    ManifestRepository manifestRepository;

    @Autowired
    PageUtil pageUtil;

    public List<Manifest> findAll() {
        return manifestRepository.findAll();
    }

    public Page<Manifest> findAllPaginated(int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        return manifestRepository.findAll(pageable);

    }

    public Page<Manifest> findPaginatedManifestByTripId(int page, int size, long tripId) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        return manifestRepository.findByTripId(pageable, tripId);
    }

    public Page<Manifest> findPaginatedManifestByScheduleId(int page, int size, long scheduleId) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        return manifestRepository.findByScheduleId(pageable, scheduleId);
    }

    public Manifest save(Manifest manifest) {
        long id = manifest.getId();
        boolean exists = manifestRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Manifest already exists");
        return manifestRepository.save(manifest);
    }

    public Iterable<Manifest> saveAll(List<Manifest> manifests) {
        return manifestRepository.saveAll(manifests);
    }

    public Manifest findById(long id) {
        return manifestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manifest does not exist"));
    }

    public Manifest update(Manifest manifest) {
        Optional<Manifest> existing = manifestRepository.findById(manifest.getId());
        if (existing.isPresent())
            return manifestRepository.save(manifest);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manifest does not exist");
    }

    public void delete(long id) {
        Optional<Manifest> existing = manifestRepository.findById(id);
        if (existing.isPresent())
            manifestRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manifest does not exist");
        }
    }

    public List<Manifest> findByTripId(long tripId) {
        return manifestRepository.findByTripId(tripId);
    }

    public Long countAll() {
        return manifestRepository.count();
    }

    public void deleteAll() {
        manifestRepository.deleteAll();
    }

}
