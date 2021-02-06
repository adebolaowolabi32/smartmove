package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.StateService;
import com.interswitch.smartmoveserver.service.TerminalService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/terminals")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private StateService stateService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        //TODO:: Implement server side pagination
        List<Terminal> terminals = terminalService.findAll(owner, principal.getName());
        model.addAttribute("terminals", terminals);
        return "terminals/get";
    }

    @GetMapping("/getlgas")
    @ResponseBody
    public List<String> getAll(@RequestParam String state) {
        return stateService.findByName(state).getLocalGovts();
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Terminal terminal = terminalService.findById(id, principal.getName());
        model.addAttribute("terminal", terminal);
        return "terminals/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Terminal terminal = new Terminal();
        model.addAttribute("terminal", terminal);
        model.addAttribute("countries", stateService.findAllCountries());
        model.addAttribute("states", stateService.findAll());
        model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("terminal")));
        return "terminals/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Terminal terminal, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("terminal", terminal);
            model.addAttribute("countries", stateService.findAllCountries());
            model.addAttribute("states", stateService.findAll());
            model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("terminal")));
            return "terminals/create";
        }

        Terminal savedTerminal = terminalService.save(terminal, principal.getName());
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/terminals/details/" + savedTerminal.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Terminal terminal = terminalService.findById(id, principal.getName());
        model.addAttribute("terminal", terminal);
        model.addAttribute("countries", stateService.findAllCountries());
        model.addAttribute("states", stateService.findAll());
        model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("terminal")));
        return "terminals/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Terminal terminal,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        terminal.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("terminal", terminal);
            model.addAttribute("countries", stateService.findAllCountries());
            model.addAttribute("states", stateService.findAll());
            model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("terminal")));
            return "terminals/update";
        }
        terminalService.update(terminal, principal.getName());
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/terminals/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {

        Terminal terminal = terminalService.findById(id, principal.getName());
        terminalService.auditedDelete(id, principal.getName());
        User owner = terminal.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/terminals/get?owner=" + ownerId;

    }
}