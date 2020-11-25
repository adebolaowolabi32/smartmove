package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.UserSettings;
import com.interswitch.smartmoveserver.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@Layout(value = "layouts/default")
@RequestMapping("/userSettings")
public class UserSettingsController {

    @Autowired
    private UserSettingsService userSettingsService;

    @GetMapping("/details")
    public String getDetails(Principal principal, Model model) {
        UserSettings userSettings = userSettingsService.findByOwner(principal.getName());
        model.addAttribute("userSettings", userSettings);
        return "userSettings/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        UserSettings userSettings = new UserSettings();
        model.addAttribute("userSettings", userSettings);
        return "userSettings/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid UserSettings userSettings, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("userSettings", userSettings);
            return "userSettings/create";
        }

        userSettingsService.save(userSettings, principal.getName());
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/userSettings/details/";
    }

    @GetMapping("/update")
    public String showUpdate(Principal principal, Model model) {
        UserSettings userSettings = userSettingsService.findByOwner(principal.getName());
        model.addAttribute("userSettings", userSettings);
        return "userSettings/update";
    }


    @PostMapping("/update")
    public String update(Principal principal, @Valid UserSettings userSettings,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("userSettings", userSettings);
            return "userSettings/update";
        }

        userSettingsService.update(userSettings, principal.getName());
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/userSettings/details/";
    }
}
