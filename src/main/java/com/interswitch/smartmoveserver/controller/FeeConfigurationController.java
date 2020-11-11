package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.service.FeeConfigurationService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@Layout(value = "layouts/default")
@RequestMapping("/fees")
public class FeeConfigurationController {

    @Autowired
    private FeeConfigurationService feeConfigurationService;

    @Autowired
    private PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        PageView<FeeConfiguration> feeConfigurationPageViewPage = feeConfigurationService.findAllPaginated(owner, page, size, principal.getName());
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(feeConfigurationPageViewPage));
        model.addAttribute("feeConfigPage", feeConfigurationPageViewPage);
        return "fees/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) {

        FeeConfiguration feeConfiguration = feeConfigurationService.findById(id,principal.getName());
        model.addAttribute("fee", feeConfiguration);
        return "fees/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        FeeConfiguration feeConfiguration = new FeeConfiguration();
        model.addAttribute("fee", feeConfiguration);
        return "fees/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid FeeConfiguration feeConfiguration, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("fee", feeConfiguration);
            return "fees/create";
        }

        FeeConfiguration savedFee = feeConfigurationService.save(feeConfiguration, principal.getName());
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/fees/details/" + savedFee.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        FeeConfiguration fee = feeConfigurationService.findById(id,principal.getName());
        model.addAttribute("fee", fee);
        return "fees/update";
    }


    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid FeeConfiguration fee,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        fee.setId(id);
        FeeConfiguration existingFee = feeConfigurationService.findById(id,principal.getName());

        if (result.hasErrors()) {
            model.addAttribute("fee", existingFee);
            return "fees/update";
        }

        fee.setFeeName(existingFee.getFeeName());
        feeConfigurationService.update(fee, principal.getName());
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/fees/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        FeeConfiguration fee = feeConfigurationService.findById(id,principal.getName());
        feeConfigurationService.delete(id, principal.getName());
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/fees/get";
    }


}
