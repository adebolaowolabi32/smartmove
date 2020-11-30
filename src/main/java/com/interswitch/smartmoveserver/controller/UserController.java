package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.*;
import com.interswitch.smartmoveserver.util.ErrorResponseUtil;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    @Autowired
    ErrorResponseUtil errorResponseUtil;

    @Autowired
    private CardService cardService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RouteService routeService;


    private final Log logger = LogFactory.getLog(getClass());

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam("role") Enum.Role role,
                         @RequestParam(required = false, defaultValue = "0") Long owner,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size, Model model) {
        PageView<User> userPage = userService.findAllPaginatedByRole(principal.getName(), owner, role, page, size);
        model.addAttribute("title", pageUtil.buildTitle(role));
        model.addAttribute("role", role);
        model.addAttribute("userPage", userPage);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(userPage));
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(role));
        return "users/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        User user = userService.findById(id, principal.getName());
        model.addAttribute("regulators_no", userService.countByRoleAndOwner(user, Enum.Role.REGULATOR));
        model.addAttribute("operators_no", userService.countByRoleAndOwner(user, Enum.Role.OPERATOR));
        model.addAttribute("ticketers_no", userService.countByRoleAndOwner(user, Enum.Role.TICKETER));
        model.addAttribute("agents_no", userService.countByRoleAndOwner(user, Enum.Role.AGENT));
        model.addAttribute("vehicles_no", vehicleService.countByOwner(user));
        model.addAttribute("terminals_no", terminalService.countByOwner(user));
        model.addAttribute("routes_no", routeService.countByOwner(user));
        model.addAttribute("devices_no", deviceService.countByOwner(user));
        model.addAttribute("transactions_no", transactionService.countAll());
        model.addAttribute("settlements_no", 0);
        model.addAttribute("cards_no", cardService.countByOwner(user));
        model.addAttribute("title", pageUtil.buildTitle(user.getRole()));
        model.addAttribute("user", user);
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(user.getRole()));
        //TODO::Get children of each entity by type
        return "users/details";
    }


    @GetMapping("/create")
    public String showCreate(Principal principal, @RequestParam("role") Enum.Role role, Model model) {
        //TODO:: need to handle method level user permissions specific to each role
        User user = new User();
        user.setRole(role);
        model.addAttribute("title", pageUtil.buildTitle(role));
        model.addAttribute("user", user);
        //TODO change findAll to findAllEligible
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(role));
        model.addAttribute("owners", userService.findAll());

        return "users/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @RequestParam("role") Enum.Role role, @Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        logger.info("Wanna create user for role==>"+role.name());
        user.setRole(role);

        if (result.hasErrors()) {
            logger.info("Error trying to create user==>"+errorResponseUtil.getErrorMessages(result));
            model.addAttribute("title", pageUtil.buildTitle(role));
            model.addAttribute("user", user);
            //TODO change findAll to findAllEligible
            model.addAttribute("isOwned", securityUtil.isOwnedEntity(role));
            model.addAttribute("owners", userService.findAll());
            return "users/create";
        }

        logger.info("wanna call user service to to create user");
        User savedUser = userService.save(user, principal.getName());

        redirectAttributes.addFlashAttribute("saved", true);
        redirectAttributes.addFlashAttribute("saved_message", pageUtil.buildSaveMessage(role));
        return "redirect:/users/details/" + savedUser.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        User user = userService.findById(id, principal.getName());
        model.addAttribute("title", pageUtil.buildTitle(user.getRole()));
        model.addAttribute("user", user);
        //TODO change findAll to findAllEligible
        model.addAttribute("owners", userService.findAll());
        model.addAttribute("isOwned", securityUtil.isOwnedEntity(user.getRole()));
        return "users/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid User user,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        User existing = userService.findById(id, principal.getName());
        Enum.Role role = existing.getRole();
        if (result.hasErrors()) {
            //TODO change findAll to findAllEligible
            model.addAttribute("isOwned", securityUtil.isOwnedEntity(role));
            model.addAttribute("title", pageUtil.buildTitle(role));
            model.addAttribute("user", existing);
            model.addAttribute("owners", userService.findAll());
            return "users/update";
        }
        userService.update(user, principal.getName());
        redirectAttributes.addFlashAttribute("updated", true);
        redirectAttributes.addFlashAttribute("updated_message", pageUtil.buildUpdateMessage(role));
        return "redirect:/users/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id, principal.getName());
        userService.delete(id, principal.getName());
        Enum.Role role = user.getRole();
        User owner = user.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        redirectAttributes.addFlashAttribute("deleted_message", pageUtil.buildDeleteMessage(role));
        return "redirect:/users/get?role=" + role + "&owner=" + ownerId;
    }

    @GetMapping("/upload")
    public String showUserUploadPage(Principal principal, Model model) {
        return "users/upload";
    }

    @PostMapping("/upload")
    public String doUserUpload(Principal principal, MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        try {
            boolean succeeded = userService.upload(file, principal.getName());
            redirectAttributes.addFlashAttribute("uploaded", succeeded);
            return "redirect:/users/get";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", false);
            return "redirect:/users/get";
        }
    }

    @GetMapping("/approvals")
    public String showApprovals(Principal principal, Model model) {
        model.addAttribute("approvals", userService.getApprovals(principal.getName()));

        return "users/approvals";
    }

    @GetMapping("/approve/{id}")
    public String approve(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        boolean approved = userService.approveUser(principal.getName(), id);

        if (!approved) redirectAttributes.addFlashAttribute("error", "Unable to approve this user.");
        else
            redirectAttributes.addFlashAttribute("success", "User has been approved and account setup email has been sent.");

        return "redirect:/users/approvals";
    }

}
