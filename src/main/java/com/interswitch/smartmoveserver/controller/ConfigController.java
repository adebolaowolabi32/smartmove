package com.interswitch.smartmoveserver.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String create(@Valid Config config, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "configurations/create";
        }
        configService.save(config);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/configurations/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Config config = configService.findById(id);
        model.addAttribute("configuration", config);
        return "configurations/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Config config,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        config.setId(id);
        if (result.hasErrors()) {
            return "configurations/update";
        }
        configService.update(config);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/configurations/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        configService.delete(id);
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/configurations/get";
    }
}
