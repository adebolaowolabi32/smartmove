package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/terminals")
public class TerminalApi {
    @Autowired
    TerminalService terminalService;

    @GetMapping(produces = "application/json")
    private Page<Terminal> findAll(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = terminalService.findAllPaginated(principal, 0L, page, size);
        return new Page<Terminal>(pageable.getTotalElements(), pageable.getContent());    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Terminal save(@Validated @RequestBody Terminal terminal, Principal principal) {
        return terminalService.save(terminal, principal);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Terminal findById(@Validated @PathVariable long id, Principal principal) {
        return terminalService.findById(id, principal);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Terminal update(@Validated @RequestBody Terminal terminal, Principal principal) {
        return terminalService.update(terminal, principal);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id, Principal principal) {
        terminalService.delete(id, principal);
    }
}
