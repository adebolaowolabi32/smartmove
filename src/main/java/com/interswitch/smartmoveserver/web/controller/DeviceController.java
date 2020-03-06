package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/get")
    public String getAll(Model model, @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Device> devicePage = deviceService.findAllPaginated(currentPage, pageSize);
        int totalPages = devicePage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
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
        model.addAttribute("owners", deviceService.getAll());
        return "devices/create";
    }

    @PostMapping("/create")
    public String create(@Valid Device device, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("device", device);
            model.addAttribute("owners", deviceService.getAll());
            return "devices/create";
        }
        deviceService.save(device);

        Page<Device> devicePage = deviceService.findAllPaginated(1, 5);
        int totalPages = devicePage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("devicePage", devicePage);
        return "redirect:/devices/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Device device = deviceService.findById(id);
        model.addAttribute("device", device);
        model.addAttribute("owners", deviceService.getAll());
        return "devices/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Device device,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            device.setId(id);
            model.addAttribute("device", device);
            model.addAttribute("owners", deviceService.getAll());
            return "devices/update";
        }

        Page<Device> devicePage = deviceService.findAllPaginated(1, 5);
        int totalPages = devicePage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("devicePage", devicePage);
        return "redirect:/devices/get";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        deviceService.delete(id);
        Page<Device> devicePage = deviceService.findAllPaginated(1, 5);
        int totalPages = devicePage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("devicePage", devicePage);
        return "redirect:/devices/get";
    }
}
