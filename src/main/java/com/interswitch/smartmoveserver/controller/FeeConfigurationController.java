package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.FeeConfiguration;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.service.FeeConfigurationService;
import com.interswitch.smartmoveserver.util.ErrorResponseUtil;
import com.interswitch.smartmoveserver.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@Layout(value = "layouts/default")
@RequestMapping("/fees")
public class FeeConfigurationController {

    @Autowired
    private FeeConfigurationService feeConfigurationService;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    ErrorResponseUtil errorResponseUtil;

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

        log.info("Entered controller...");
        if (result.hasErrors()) {
            log.info("has Entered controller error..."+errorResponseUtil.getErrorMessages(result));
            model.addAttribute("fee", feeConfiguration);
            String message = errorResponseUtil.getErrorMessages(result).contains("Failed to convert property value of type 'java.lang.String")?"Fee value only accept numbers and not texts." :"Unacceptable data format for one of the fields is used,please check and retry.";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/fees/create";
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
        FeeConfiguration existingFee = feeConfigurationService.findById(id, principal.getName());
        fee.setFeeName(existingFee.getFeeName());

        if (result.hasErrors()) {
            log.info("there's an error with fee config update" + result.getFieldErrors());
            model.addAttribute("fee", existingFee);
            return "fees/update";
        }
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
