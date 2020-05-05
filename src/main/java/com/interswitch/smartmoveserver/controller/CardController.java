package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.annotation.Layout;
import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.service.CardService;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.PageUtil;
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
        Page<Card> cardPage = cardService.findAllPaginated(page, size);
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
    public String create(Principal principal, @Valid Card card, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("card", card);
            model.addAttribute("owners", userService.findAll());
            return "cards/create";
        }
        cardService.save(card);
        Page<Card> cardPage = cardService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(cardPage));
        model.addAttribute("cardPage", cardPage);
        model.addAttribute("saved", true);
        return "redirect:/cards/get";
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
                         BindingResult result, Model model) {
        card.setId(id);
        model.addAttribute("card", card);
        if (result.hasErrors()) {
            model.addAttribute("owners", userService.findAll());
            return "cards/update";
        }
        cardService.update(card);
        model.addAttribute("updated", true);
        return "redirect:/cards/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") long id, Model model) {
        cardService.delete(id);
        Page<Card> cardPage = cardService.findAllPaginated(1, 10);
        model.addAttribute("pageNumbers", pageUtil.getPageNumber(cardPage));
        model.addAttribute("cardPage", cardPage);
        model.addAttribute("deleted", true);
        return "redirect:/cards/get";
    }
}
