package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.DeviceService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.service.VehicleService;
import com.interswitch.smartmoveserver.web.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
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

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Model model, @RequestParam("role") Enum.Role role,@RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<User> userPage = userService.findByRolePaginated(page, size, role);
        model.addAttribute("userPage", userPage);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(userPage));
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
    public String showCreate(@RequestParam("role") Enum.Role role, Model model) {
        User user = new User();
        model.addAttribute("title", buildTitle(role));
        model.addAttribute("user", user);
        model.addAttribute("owners", userService.getAll());
        model.addAttribute("role", role);
        return "users/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @RequestParam("role") Enum.Role role, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", buildTitle(user.getRole()));
            model.addAttribute("owners", userService.getAll());
            model.addAttribute("user", user);
            model.addAttribute("role", role);
            return "users/create";
        }
        user.setRole(role);
        userService.save(user, principal);
        Page<User> userPage = userService.getAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(userPage));
        model.addAttribute("title", buildTitle(user.getRole()));
        model.addAttribute("userPage", userPage);
        return "redirect:/users/get?role=" + role;
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
        model.addAttribute("role", user.getRole());
        return "redirect:/users/details/" + id;
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
        Enum.Role role = user.getRole();
        model.addAttribute("userPage", userPage);
        model.addAttribute("title", buildTitle(role));
        model.addAttribute("role", role);
        return "redirect:/users/get?role=" + role;
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
