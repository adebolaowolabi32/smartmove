package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.service.DeviceService;
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
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<Device> devicePage = deviceService.findAllPaginated(page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(devicePage));
        model.addAttribute("devicePage", devicePage);
        return "devices/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        Device device = deviceService.findById(id);
        model.addAttribute("device", device);
        return "devices/details";
    }

    @GetMapping("/create")
    public String showCreate(Model model) {
        Device device = new Device();
        model.addAttribute("device", device);
        model.addAttribute("owners", userService.getAll());
        return "devices/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Device device, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("device", device);
            model.addAttribute("owners", userService.getAll());
            return "devices/create";
        }
        deviceService.save(device, principal);
        Page<Device> devicePage = deviceService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(devicePage));
        model.addAttribute("devicePage", devicePage);
        return "redirect:/devices/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Device device = deviceService.findById(id);
        model.addAttribute("device", device);
        model.addAttribute("owners", userService.getAll());
        return "devices/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Device device,
                         BindingResult result, Model model) {
        device.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("device", device);
            model.addAttribute("owners", userService.getAll());
            return "devices/update";
        }
        deviceService.update(device);
        return "redirect:/devices/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        deviceService.delete(id);
        Page<Device> devicePage = deviceService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(devicePage));
        model.addAttribute("devicePage", devicePage);
        return "redirect:/devices/get";
    }
}
