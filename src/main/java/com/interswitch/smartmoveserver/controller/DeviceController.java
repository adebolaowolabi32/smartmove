package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.view.FundDevice;
import com.interswitch.smartmoveserver.service.DeviceService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String findAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        PageView<Device> devicePage = deviceService.findAllPaginated(owner, page, size, principal.getName());
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(devicePage));
        model.addAttribute("devicePage", devicePage);
        return "devices/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Device device = deviceService.findById(id, principal.getName());
        model.addAttribute("device", device);
        return "devices/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Device device = new Device();
        model.addAttribute("device", device);
        model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("device")));
        return "devices/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Device device, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("device", device);
            model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("device")));
            return "devices/create";
        }
        Device savedDevice = deviceService.save(device, principal.getName());
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/devices/details/" + savedDevice.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Device device = deviceService.findById(id, principal.getName());
        model.addAttribute("device", device);
        model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("device")));
        return "devices/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Device device,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        device.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("device", device);
            model.addAttribute("owners", userService.findOwners(pageUtil.getOwners("device")));
            return "devices/update";
        }
        deviceService.update(device, principal.getName());
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/devices/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Device device = deviceService.findById(id, principal.getName());
        deviceService.delete(id, principal.getName());
        User owner = device.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/devices/get?owner=" + ownerId;
    }

    @GetMapping("/fund/{id}")
    public String transfer(Principal principal, @PathVariable("id") long id, Model model) {
        FundDevice fundDevice = new FundDevice();
        fundDevice.setDeviceId(id);
        model.addAttribute("fundDevice", fundDevice);
        return "devices/fund";
    }

    @PostMapping("/fund/{id}")
    public String transfer(Principal principal, @PathVariable("id") long id, @Valid FundDevice fundDevice, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        fundDevice.setDeviceId(id);
        if (result.hasErrors()) {
            model.addAttribute("fundDevice", new FundDevice());
            return "devices/fund";
        }
        deviceService.fundDevice(principal.getName(), fundDevice);
        redirectAttributes.addFlashAttribute("funded", true);
        return "redirect:/devices/details/" + fundDevice.getDeviceId();
    }

    @GetMapping("/upload")
    public String showDevicesUploadPage(Principal principal, Model model) {
        return "devices/upload";
    }


    @PostMapping("/upload")
    public String doDevicesUpload(Principal principal, MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        try {
            boolean succeeded = deviceService.upload(file,principal.getName());
            redirectAttributes.addFlashAttribute("uploaded", succeeded);
            return "redirect:/devices/get";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", false);
            return "redirect:/devices/get";
        }
    }
}
