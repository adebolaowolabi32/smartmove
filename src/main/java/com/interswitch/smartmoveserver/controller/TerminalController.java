package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.TerminalService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<Terminal> terminalPage = terminalService.findAllPaginated(page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(terminalPage));
        model.addAttribute("terminalPage", terminalPage);
        return "terminals/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Terminal terminal = terminalService.findById(id);
        model.addAttribute("terminal", terminal);
        return "terminals/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Terminal terminal = new Terminal();
        model.addAttribute("terminal", terminal);
        model.addAttribute("owners", userService.findAll());
        return "terminals/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Terminal terminal, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("terminal", terminal);
            model.addAttribute("owners", userService.findAll());
            return "terminals/create";
        }
        Terminal savedTerminal = terminalService.save(terminal, principal);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/terminals/details/" + savedTerminal.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Terminal terminal = terminalService.findById(id);
        model.addAttribute("terminal", terminal);
        model.addAttribute("owners", userService.findAll());
        return "terminals/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Terminal terminal,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        terminal.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("terminal", terminal);
            model.addAttribute("owners", userService.findAll());
            return "terminals/update";
        }
        terminalService.update(terminal);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/terminals/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Terminal terminal = terminalService.findById(id);
        terminalService.delete(id);
        User owner = terminal.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/terminals/get?owner=" + ownerId;
    }
}