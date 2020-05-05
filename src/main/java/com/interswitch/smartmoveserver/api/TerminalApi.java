package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/terminals")
public class TerminalApi {
    @Autowired
    TerminalService terminalService;

    @GetMapping(produces = "application/json")
    private List<Terminal> getAll() {
        return terminalService.getAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Terminal save(@Validated @RequestBody Terminal terminal) {
        return terminalService.save(terminal);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Terminal findById(@Validated @PathVariable long id) {
        return terminalService.findById(id);
    }

    @GetMapping(value = "/findByType/{type}", produces = "application/json")
    private List<Terminal> find(@Validated @PathVariable Enum.TransportMode type) {
        return terminalService.find(type);
    }

    @GetMapping(value = "/findByOwner/{owner}", produces = "application/json")
    private List<Terminal> findByOwner(@Validated @PathVariable long owner) {
        return terminalService.findByOwner(owner);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Terminal update(@Validated @RequestBody Terminal terminal) {
        return terminalService.update(terminal);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        terminalService.delete(id);
    }

    @PostMapping(value = "/activate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void activate(@Validated @PathVariable long id) {
        terminalService.activate(id);
    }

    @PostMapping(value = "/deactivate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void deactivate(@Validated @PathVariable long id) {
        terminalService.deactivate(id);
    }
}
