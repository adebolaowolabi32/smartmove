package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/configurations")
public class ConfigApi {
    @Autowired
    private ConfigService configService;
    
    @GetMapping(produces = "application/json")
    private List<Config> getAll() {
        return configService.getAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Config save(@Validated @RequestBody Config config) {
        return configService.save(config);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Config findById(@Validated @PathVariable long id) {
        return configService.findById(id);
    }

    @GetMapping(value = "/findByName/{name}", produces = "application/json")
    private Config findByName(@Validated @PathVariable Enum.ConfigList name) {
        return configService.findByName(name);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Config update(@Validated @RequestBody Config config) {
        return configService.update(config);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        configService.delete(id);
    }

}
