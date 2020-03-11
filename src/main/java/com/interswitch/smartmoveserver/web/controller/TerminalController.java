package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.service.TerminalService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.web.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@Controller
@Layout(value = "layouts/default")
@RequestMapping("/terminals")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<Terminal> terminalPage = terminalService.findAllPaginated(page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(terminalPage));
        model.addAttribute("terminalPage", terminalPage);
        return "terminals/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        Terminal terminal = terminalService.findById(id);
        model.addAttribute("terminal", terminal);
        return "terminals/details";
    }

    @GetMapping("/create")
    public String showCreate(Model model) {
        Terminal terminal = new Terminal();
        model.addAttribute("terminal", terminal);
        model.addAttribute("owners", userService.getAll());
        return "terminals/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Terminal terminal, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("terminal", terminal);
            model.addAttribute("owners", userService.getAll());
            return "terminals/create";
        }
        terminalService.save(terminal, principal);
        Page<Terminal> terminalPage = terminalService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(terminalPage));
        model.addAttribute("terminalPage", terminalPage);
        return "redirect:/terminals/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Terminal terminal = terminalService.findById(id);
        model.addAttribute("terminal", terminal);
        model.addAttribute("owners", userService.getAll());
        return "terminals/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Terminal terminal,
                         BindingResult result, Model model) {
        terminal.setId(id);
        model.addAttribute("terminal", terminal);
        if (result.hasErrors()) {
            model.addAttribute("owners", userService.getAll());
            return "terminals/update";
        }
        terminalService.update(terminal);
        return "redirect:/terminals/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        terminalService.delete(id);
        Page<Terminal> terminalPage = terminalService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(terminalPage));
        model.addAttribute("terminalPage", terminalPage);
        return "redirect:/terminals/get";
    }
}