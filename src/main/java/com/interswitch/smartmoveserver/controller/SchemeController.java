package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Scheme;
import com.interswitch.smartmoveserver.service.SchemeService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.PageUtil;
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
import java.security.Principal;
import java.util.Set;

/*
 * Created by adebola.owolabi on 6/18/2020
 */
@Controller
@RequestMapping("/schemes")
public class SchemeController {
    @Autowired
    private SchemeService schemeService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, Model model) {
        Set<Scheme> schemes = schemeService.getSchemes();
        model.addAttribute("schemes", schemes);
        return "schemes/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Scheme scheme = schemeService.findScheme(String.valueOf(id));
        model.addAttribute("scheme", scheme);
        return "schemes/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Scheme scheme = new Scheme();
        model.addAttribute("scheme", scheme);
        model.addAttribute("users", userService.findAll());
        return "schemes/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Scheme scheme, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("scheme", scheme);
            model.addAttribute("users", userService.findAll());
            return "schemes/create";
        }
        schemeService.createScheme(scheme);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/schemes/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") int id, Model model) {
        Scheme scheme = schemeService.findScheme(String.valueOf(id));
        model.addAttribute("scheme", scheme);
        model.addAttribute("users", userService.findAll());
        return "schemes/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") int id, @Valid Scheme scheme,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        scheme.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("scheme", scheme);
            model.addAttribute("users", userService.findAll());
            return "schemes/update";
        }
        schemeService.updateScheme(scheme);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/schemes/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        schemeService.deleteScheme(String.valueOf(id));
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/schemes/get";
    }
}
