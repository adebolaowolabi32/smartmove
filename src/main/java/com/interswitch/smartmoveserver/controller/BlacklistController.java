package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Blacklist;
import com.interswitch.smartmoveserver.service.BlacklistService;
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

/*
 * Created by adebola.owolabi on 5/21/2020
 */
@Controller
@Layout(value = "layouts/default")
@RequestMapping("/blacklists")
public class BlacklistController {
        @Autowired
        private BlacklistService blacklistService;

        @Autowired
        private UserService userService;

        @Autowired
        PageUtil pageUtil;

        @GetMapping("/get")
        public String getAll(Principal principal, Model model, @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) {
            Page<Blacklist> blacklistPage = blacklistService.findAllPaginated(page, size);
            model.addAttribute("pageNumbers", pageUtil.getPageNumber(blacklistPage));
            model.addAttribute("blacklistPage", blacklistPage);
            return "blacklists/get";
        }

        @GetMapping("/details/{id}")
        public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
            Blacklist blacklist = blacklistService.findById(id);
            model.addAttribute("blacklist", blacklist);
            return "blacklists/details";
        }

        @GetMapping("/add")
        public String showAdd(Principal principal, Model model) {
            Blacklist blacklist = new Blacklist();
            model.addAttribute("blacklist", blacklist);
            return "blacklists/add";
        }

        @PostMapping("/add")
        public String add(Principal principal, @Valid Blacklist blacklist, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
            if (result.hasErrors()) {
                model.addAttribute("blacklist", blacklist);
                return "blacklists/add";
            }
            blacklistService.add(blacklist);
            redirectAttributes.addFlashAttribute("saved", true);
            return "redirect:/blacklists/get";
        }

        @GetMapping("/remove/{id}")
        public String remove(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
            blacklistService.remove(id);
            redirectAttributes.addFlashAttribute("deleted", true);
            return "redirect:/blacklists/get";
        }
}
