package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.VehicleCategory;
import com.interswitch.smartmoveserver.model.VehicleModel;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.VehicleCategoryService;
import com.interswitch.smartmoveserver.service.VehicleMakeService;
import com.interswitch.smartmoveserver.service.VehicleModelService;
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
@RequestMapping("/vehicleCategories")
public class VehicleCategoryController {
    @Autowired
    VehicleCategoryService vehicleCategoryService;

    @Autowired
    VehicleMakeService vehicleMakeService;

    @Autowired
    VehicleModelService vehicleModelService;

    @Autowired
    UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size, Model model) {
        PageView<VehicleCategory> vehicleCategoryPage = vehicleCategoryService.findAllPaginated(owner, page, size, principal.getName());
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(vehicleCategoryPage));
        model.addAttribute("vehicleCategoryPage", vehicleCategoryPage);
        return "vehicleCategories/get";
    }

    @GetMapping("/getmodels")
    @ResponseBody
    public List<VehicleModel> getAll(@RequestParam long make) {
        return vehicleModelService.findAllByMake(make);
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        VehicleCategory vehicleCategory = vehicleCategoryService.findById(id, principal.getName());
        model.addAttribute("vehicleCategory", vehicleCategory);
        return "vehicleCategories/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        VehicleCategory vehicleCategory = new VehicleCategory();
        model.addAttribute("vehicleCategory", vehicleCategory);
        //TODO change findAll to findAllEligible
        model.addAttribute("owners", userService.findAll());
        model.addAttribute("makes",  vehicleMakeService.findAll());
        model.addAttribute("models", vehicleModelService.findAll());
        return "vehicleCategories/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid VehicleCategory vehicleCategory, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("vehicleCategory", vehicleCategory);
            //TODO change findAll to findAllEligible
            model.addAttribute("makes",  vehicleMakeService.findAll());
            model.addAttribute("models", vehicleModelService.findAll());
            model.addAttribute("owners", userService.findAll());
            return "vehicleCategories/create";
        }
        VehicleCategory savedVehicleCategory = vehicleCategoryService.save(vehicleCategory, principal.getName());
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/vehicleCategories/details/" + savedVehicleCategory.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        VehicleCategory vehicleCategory = vehicleCategoryService.findById(id, principal.getName());
        model.addAttribute("vehicleCategory", vehicleCategory);
        //TODO change findAll to findAllEligible
        model.addAttribute("makes",  vehicleMakeService.findAll());
        model.addAttribute("models", vehicleModelService.findAll());
        model.addAttribute("owners", userService.findAll());
        return "vehicleCategories/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid VehicleCategory vehicleCategory,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        vehicleCategory.setId(id);
        if (result.hasErrors()) {
            //TODO change findAll to findAllEligible
            model.addAttribute("vehicleCategory", vehicleCategory);
            model.addAttribute("makes",  vehicleMakeService.findAll());
            model.addAttribute("models", vehicleModelService.findAll());
            model.addAttribute("owners", userService.findAll());
            return "vehicleCategories/update";
        }
        vehicleCategoryService.update(vehicleCategory, principal.getName());
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/vehicleCategories/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        VehicleCategory vehicleCategory = vehicleCategoryService.findById(id, principal.getName());
        vehicleCategoryService.delete(id, principal.getName());
        User owner = vehicleCategory.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/vehicleCategories/get?owner=" + ownerId;
    }
}
