package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Document;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.DocumentService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.VehicleService;
import com.interswitch.smartmoveserver.util.FilefileOpsUtil;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.Multipart;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    VehicleService vehicleService;

    @Autowired
    UserService userService;

    @Autowired
    DocumentService docService;

    @Autowired
    PageUtil pageUtil;

    Set<String> facilities = new HashSet<>();


    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size, Model model) {
        Page<Vehicle> vehiclePage = vehicleService.findAllPaginated(principal, owner, page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(vehiclePage));
        model.addAttribute("vehiclePage", vehiclePage);
        return "vehicles/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Vehicle vehicle = vehicleService.findById(principal, id);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("allFacilities", vehicle.getFacilities());
        return "vehicles/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Vehicle vehicle = new Vehicle();
        model.addAttribute("vehicle", vehicle);
        //TODO change findAll to findAllEligible
        model.addAttribute("owners", userService.findAll());
        facilities.addAll(Arrays.asList("Air Conditioner","Media","Toilet","Power","Excess Luggage"));
        model.addAttribute("allFacilities", facilities);
        return "vehicles/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Vehicle vehicle, MultipartFile file, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        Document vehiclePic = null;
        try{
            if (result.hasErrors()) {
                model.addAttribute("vehicle", vehicle);
                //TODO change findAll to findAllEligible
                model.addAttribute("owners", userService.findAll());
                facilities.addAll(Arrays.asList("Air Conditioner","Media","Toilet","Power","Excess Luggage"));
                model.addAttribute("allFacilities", facilities);
                return "vehicles/create";
            }

            if(file!=null){
                vehiclePic = new Document(file);
            }

            vehicle.setVehiclePicture(vehiclePic);
            Vehicle savedVehicle = vehicleService.save(vehicle);
            redirectAttributes.addFlashAttribute("saved", true);
            return "redirect:/vehicles/details/" + savedVehicle.getId();

        }catch(IOException ex){
            logger.error(ex.getMessage());
            return "vehicles/create";
        }

    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Vehicle vehicle = vehicleService.findById(principal, id);
        model.addAttribute("vehicle", vehicle);
        //TODO change findAll to findAllEligible
        facilities.addAll(Arrays.asList("Air Conditioner","Media","Toilet","Power","Excess Luggage"));
        model.addAttribute("allFacilities", facilities);
        model.addAttribute("owners", userService.findAll());
        return "vehicles/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Vehicle vehicle,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        vehicle.setId(id);
        if (result.hasErrors()) {
            //TODO change findAll to findAllEligible
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("owners", userService.findAll());
            facilities.addAll(Arrays.asList("Air Conditioner","Media","Toilet","Power","Excess Luggage"));
            model.addAttribute("allFacilities", facilities);
            return "vehicles/update";
        }
        vehicleService.update(vehicle);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/vehicles/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Vehicle vehicle = vehicleService.findById(id);
        vehicleService.delete(id);
        User owner = vehicle.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/vehicles/get?owner=" + ownerId;
    }

    @GetMapping( value="/picture/{id}",produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @ResponseBody
    public byte [] getVehiclePicture(Principal principal, @PathVariable("id") long id, Model model) {
        return docService.getDocumentById(id).getData();
    }

    @GetMapping( value="/pic/{id}",produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @ResponseBody
    public String getVehiclePic(Principal principal, @PathVariable("id") long id, Model model) {

        String base64 =  docService.getDocumentById(id).getBase64Data();
        model.addAttribute("base64", base64);
        return "vehicles/pic";
    }
}
