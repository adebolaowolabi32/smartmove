package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/routes")
public class RouteApi {
    @Autowired
    RouteService routeService;

    @GetMapping(produces = "application/json")
    private Page<Route> findAll(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size, Principal principal) {

        org.springframework.data.domain.Page pageable = routeService.findAllPaginated(principal, 0L, page, size);
        return new Page<Route>(pageable.getTotalElements(), pageable.getContent());    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Route save(@Validated @RequestBody Route route, Principal principal) {
        return routeService.save(route, principal);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Route findById(@Validated @PathVariable long id, Principal principal) {
        return routeService.findById(id, principal);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Route update(@Validated @RequestBody Route route, Principal principal) {
        return routeService.update(route, principal);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id, Principal principal) {
        routeService.delete(id, principal);
    }
}
