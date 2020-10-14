package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Blacklist;
import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/*
 * Created by adebola.owolabi on 10/5/2020
 */
@RestController
@RequestMapping("/api/blacklists")
public class BlacklistApi {
    @Autowired
    BlacklistService blacklistService;

    @GetMapping(produces = "application/json")
    private Page<Blacklist> findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        org.springframework.data.domain.Page pageable = blacklistService.findAllPaginated(page, size);
        return new Page<Blacklist>(pageable.getTotalElements(), pageable.getContent());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Blacklist save(@Validated @RequestBody Blacklist blacklist) {
        return blacklistService.add(blacklist);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Blacklist findById(@Validated @PathVariable long id) {
        return blacklistService.findById(id);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        blacklistService.remove(id);
    }

}
