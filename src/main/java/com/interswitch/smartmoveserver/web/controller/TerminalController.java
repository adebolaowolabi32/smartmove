package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.service.TerminalService;
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
@RequestMapping("/terminals")
public class TerminalController {
    @Autowired
    TerminalService terminalService;

    @GetMapping("/get")
    public String getAll(Model model) {
        model.addAttribute("terminals", terminalService.getAll());
        return "terminals/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        Terminal terminal = terminalService.findById(id);
        model.addAttribute("terminal", terminal);
        return "terminals/details/" + id;
    }

    @GetMapping("/add")
    public String showCreate(Model model) {
        return "terminals/create";
    }

    @PostMapping("/add")
    public String create(@Valid Terminal terminal, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "terminals/create";
        }
        terminalService.save(terminal);
        model.addAttribute("terminals", terminalService.getAll());
        return "terminals/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Terminal terminal = terminalService.findById(id);
        model.addAttribute("terminal", terminal);
        return "terminals/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Terminal terminal,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            terminal.setId(id);
            return "terminals/update";
        }

        terminalService.update(terminal);
        model.addAttribute("terminals", terminalService.getAll());
        return "terminals/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        terminalService.delete(id);
        model.addAttribute("terminals", terminalService.getAll());
        return "terminals/get";
    }
}
