package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Manifest;
import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.service.ManifestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/manifests")
public class ManifestApi {

    @Autowired
    ManifestService manifestService;

    @GetMapping(produces = "application/json")
    private Page<Manifest> findAll(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = manifestService.findAllPaginated(principal, page, size);
        return new Page<Manifest>(pageable.getTotalElements(), pageable.getContent());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Manifest findById(@Validated @PathVariable long id, Principal principal) {
        return manifestService.findById(id, principal);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Manifest save(@Validated @RequestBody Manifest manifest, Principal principal) {
        return manifestService.save(manifest, principal);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Manifest update(@Validated @RequestBody Manifest manifest, Principal principal) {
        return manifestService.update(manifest, principal);
    }
    
    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void delete(@Validated @PathVariable long id, Principal principal) {
        manifestService.delete(id, principal);
    }
}
