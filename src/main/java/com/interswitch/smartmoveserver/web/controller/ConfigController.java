package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/configurations")
public class ConfigController {
    @Autowired
    private ConfigService configService;
    @GetMapping("/get")
    public String getAll(Model model) {
        model.addAttribute("configs", configService.getAll());
        return "configs/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        Config config = configService.findById(id);
        model.addAttribute("config", config);
        return "configs/details/" + id;
    }

    @GetMapping("/add")
    public String showCreate(Model model) {
        return "createconfig";
    }

    @PostMapping("/add")
    public String create(@Valid Config config, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "configs/create";
        }
        configService.save(config);
        model.addAttribute("configs", configService.getAll());
        return "configs/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Config config = configService.findById(id);
        model.addAttribute("config", config);
        return "configs/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Config config,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            config.setId(id);
            return "configs/update";
        }

        configService.update(config);
        model.addAttribute("configs", configService.getAll());
        return "configs/get";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        configService.delete(id);
        model.addAttribute("configs", configService.getAll());
        return "configs/get";
    }
}
