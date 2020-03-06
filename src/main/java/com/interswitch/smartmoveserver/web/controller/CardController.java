package com.interswitch.smartmoveserver.web.controller;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author adebola.owolabi
 */
@Controller
@RequestMapping("/cards")
public class CardController {
    @Autowired
    CardService cardService;

    @GetMapping("/get")
    public String getAll(Model model) {
        model.addAttribute("cards", cardService.getAll());
        return "cards/get";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") long id, Model model) {
        Card card = cardService.findById(id);
        model.addAttribute("card", card);
        return "cards/details/" + id;
    }

    @GetMapping("/add")
    public String showCreate(Model model) {
        return "cards/create";
    }

    @PostMapping("/add")
    public String create(@Valid Card card, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "cards/create";
        }
        cardService.save(card);
        model.addAttribute("cards", cardService.getAll());
        return "cards/get";
    }

    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        Card card = cardService.findById(id);
        model.addAttribute("card", card);
        return "cards/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Card card,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            card.setId(id);
            return "cards/update";
        }

        cardService.update(card);
        model.addAttribute("cards", cardService.getAll());
        return "cards/get";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        cardService.delete(id);
        model.addAttribute("cards", cardService.getAll());
        return "cards/get";
    }
}
