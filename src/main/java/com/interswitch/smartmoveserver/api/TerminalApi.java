package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.service.TerminalService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/terminals")
public class TerminalApi {
    @Autowired
    TerminalService terminalService;

    @GetMapping(produces = "application/json")
    private PageView<Terminal> findAll(@RequestParam(required = false, defaultValue = "0") Long owner, @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return terminalService.findAllPaginated(owner, page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Terminal save(@Validated @RequestBody Terminal terminal) {
        return terminalService.save(terminal, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Terminal findById(@Validated @PathVariable long id) {
        return terminalService.findById(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/owner/{id}", produces = "application/json")
    private List<Terminal> findByOwnerId(@Validated @PathVariable long ownerId) {
        return terminalService.findTerminalsByOwnerId(ownerId);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Terminal update(@Validated @RequestBody Terminal terminal) {
        return terminalService.update(terminal, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        terminalService.delete(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}
