package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
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
@Layout(value = "layouts/default")
@RequestMapping("/configurations")
public class ConfigController {
    @Autowired
    private ConfigService configService;
    @GetMapping("/get")
    public String getAll(Model model) {
        model.addAttribute("configurations", configService.getAll());
        return "configurations/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        Config config = configService.findById(id);
        model.addAttribute("configuration", config);
        return "configurations/details/" + id;
    }

    @GetMapping("/create")
    public String showCreate(Model model) {
        Config config = new Config();
        model.addAttribute("configuration", config);
        return "configurations/create";
    }

    @PostMapping("/create")
    public String create(@Valid Config config, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "configurations/create";
        }
        configService.save(config);
        model.addAttribute("configurations", configService.getAll());
        model.addAttribute("saved", true);
        return "configurations/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Config config = configService.findById(id);
        model.addAttribute("configuration", config);
        return "configurations/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Config config,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            config.setId(id);
            return "configurations/update";
        }

        configService.update(config);
        model.addAttribute("updated", true);
        return "redirect:/configurations/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        configService.delete(id);
        model.addAttribute("configurations", configService.getAll());
        model.addAttribute("deleted", true);
        return "configurations/get";
    }
}
