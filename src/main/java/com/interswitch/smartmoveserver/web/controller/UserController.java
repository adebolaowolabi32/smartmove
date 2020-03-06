package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.DeviceService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author adebola.owolabi
 */
@Controller
@Layout(value = "layouts/default")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private DeviceService deviceService;


    @Autowired
    private VehicleService vehicleService;

    Enum.Role role;

    @GetMapping("/get")
    public String getAll(Model model, @RequestParam("role") Enum.Role role, @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        this.role = role;
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<User> userPage = userService.findByRolePaginated(currentPage, pageSize, role);
        int totalPages = userPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("userPage", userPage);
        model.addAttribute("title", buildTitle(role));
        model.addAttribute("role", role);
        return "users/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("title", buildTitle(user.getRole()));
        model.addAttribute("user", user);
        model.addAttribute("devices", deviceService.findAllByOwner(user));
        model.addAttribute("vehicles", vehicleService.findAllByOwner(user));
        //get children by type
        return "users/details";
    }


    @GetMapping("/create")
    @Layout("layouts/default")
    public String showCreate(@RequestParam("role") Enum.Role role, Model model) {
        User user = new User();
        user.setRole(role);
        model.addAttribute("title", buildTitle(role));
        model.addAttribute("user", user);
        model.addAttribute("owners", userService.getAll());
        return "users/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", buildTitle(user.getRole()));
            model.addAttribute("owners", userService.getAll());
            model.addAttribute("user", user);
            return "users/create";
        }
        User parent = userService.findByUsername(principal.getName());
        userService.save(user, parent);
        Page<User> userPage = userService.getAllPaginated(1, 5);

        int totalPages = userPage.getTotalPages();

        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("title", buildTitle(user.getRole()));
        model.addAttribute("userPage", userPage);
        return "redirect:/users/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("title", buildTitle(user.getRole()));
        model.addAttribute("user", user);
        model.addAttribute("owners", userService.getAll());
        return "users/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid User user,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            model.addAttribute("title", buildTitle(user.getRole()));
            model.addAttribute("user", user);
            model.addAttribute("owners", userService.getAll());
            return "users/update";
        }

        userService.update(user);
        model.addAttribute("title", buildTitle(user.getRole()));
        Page<User> userPage = userService.getAllPaginated(1, 5);
        int totalPages = userPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("userPage", userPage);
        model.addAttribute("title", buildTitle(user.getRole()));
        model.addAttribute("role", user.getRole());
        return "redirect:/users/get";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        userService.delete(id);
        Page<User> userPage = userService.getAllPaginated(1, 5);
        int totalPages = userPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("userPage", userPage);
        model.addAttribute("title", buildTitle(user.getRole()));
        model.addAttribute("role", user.getRole());
        return "redirect:/users/get";
    }

    public String buildTitle(Enum.Role role){
        String title = "";
        switch(role)
        {
            case ISW_ADMIN:
                title = "Administrator";
                break;
            case REGULATOR:
                title = "Regulator";
                break;
            case OPERATOR:
                title = "Operator";
                break;
            case VEHICLE_OWNER:
                title = "Vehicle Owner";
                break;
            case AGENT:
                title = "Agent";
                break;
            default:
                title = "No Title";
                break;
        }
        return title;
    }
}
