package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Manifest;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.service.ManifestService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manifests")
public class ManifestApi {

    @Autowired
    ManifestService manifestService;

    @GetMapping(produces = "application/json")
    private PageView<Manifest> findAll(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return manifestService.findAllPaginated(page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Manifest findById(@Validated @PathVariable long id) {
        return manifestService.findById(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Manifest save(@Validated @RequestBody Manifest manifest) {
        return manifestService.save(manifest, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Manifest update(@Validated @RequestBody Manifest manifest) {
        return manifestService.update(manifest, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    private void delete(@Validated @PathVariable long id) {
        manifestService.delete(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}
