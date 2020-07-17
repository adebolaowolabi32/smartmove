package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.DeviceService;
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
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(required = false) Enum.DeviceType type, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<Device> devicePage = deviceService.findAllPaginatedByType(principal, owner, type, page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(devicePage));
        model.addAttribute("devicePage", devicePage);
        return "devices/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Device device = deviceService.findById(id);
        model.addAttribute("device", device);
        return "devices/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Device device = new Device();
        model.addAttribute("device", device);
        model.addAttribute("owners", userService.findAll());
        return "devices/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Device device, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("device", device);
            model.addAttribute("owners", userService.findAll());
            return "devices/create";
        }
        Device savedDevice = deviceService.save(device, principal);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/devices/details/" + savedDevice.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Device device = deviceService.findById(id);
        model.addAttribute("device", device);
        model.addAttribute("owners", userService.findAll());
        return "devices/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Device device,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        device.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("device", device);
            model.addAttribute("owners", userService.findAll());
            return "devices/update";
        }
        deviceService.update(device);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/devices/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Device device = deviceService.findById(id);
        deviceService.delete(id);
        User owner = device.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/devices/get?owner=" + ownerId;
    }
}
