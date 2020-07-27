package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.CardService;
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
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @Autowired
    PageUtil pageUtil;

    @GetMapping("/get")
    public String getAll(Principal principal, @RequestParam(required = false, defaultValue = "0") Long owner,
                         Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<Card> cardPage = cardService.findAllPaginated(principal, owner, page, size);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(cardPage));
        model.addAttribute("cardPage", cardPage);
        return "cards/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Principal principal, @PathVariable("id") long id, Model model) {
        Card card = cardService.findById(id);
        model.addAttribute("card", card);
        return "cards/details";
    }

    @GetMapping("/create")
    public String showCreate(Principal principal, Model model) {
        Card card = new Card();
        model.addAttribute("card", card);
        model.addAttribute("owners", userService.findAll());
        return "cards/create";
    }

    @PostMapping("/create")
    public String create(Principal principal, @Valid Card card, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("card", card);
            model.addAttribute("owners", userService.findAll());
            return "cards/create";
        }
        Card savedCard = cardService.save(card);
        redirectAttributes.addFlashAttribute("saved", true);
        return "redirect:/cards/details/" + savedCard.getId();
    }

    @GetMapping("/update/{id}")
    public String showUpdate(Principal principal, @PathVariable("id") long id, Model model) {
        Card card = cardService.findById(id);
        model.addAttribute("card", card);
        model.addAttribute("owners", userService.findAll());
        return "cards/update";
    }

    @PostMapping("/update/{id}")
    public String update(Principal principal, @PathVariable("id") long id, @Valid Card card,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        card.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("card", card);
            model.addAttribute("owners", userService.findAll());
            return "cards/update";
        }
        cardService.update(card);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/cards/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Card card = cardService.findById(id);
        cardService.delete(id);
        User owner = card.getOwner();
        long ownerId = owner != null ? owner.getId() : 0;
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/cards/get?owner=" + ownerId;
    }
}
