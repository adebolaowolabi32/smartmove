package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.RouteService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/routes")
public class RouteApi {
    @Autowired
    RouteService routeService;

    @GetMapping(produces = "application/json")
    private PageView<Route> findAll(@RequestParam(required = false, defaultValue = "0") Long owner, @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return routeService.findAllPaginated(owner, page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Route save(@Validated @RequestBody Route route) {
        return routeService.save(route, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Route findById(@Validated @PathVariable long id) {
        return routeService.findById(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Route update(@Validated @RequestBody Route route) {
        return routeService.update(route, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    private void delete(@Validated @PathVariable long id) {
        routeService.delete(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}
