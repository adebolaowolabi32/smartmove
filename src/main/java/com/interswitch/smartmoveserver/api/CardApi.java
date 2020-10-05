package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/cards")
public class CardApi {
    @Autowired
    CardService cardService;

    @GetMapping(produces = "application/json")
    private Page<Card> findAll(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = cardService.findAllPaginated(principal, 0L, page, size);
        return new Page<Card>(pageable.getTotalElements(), pageable.getContent());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Card save(@Validated @RequestBody Card card, Principal principal) {
        return cardService.save(card, principal);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Card findById(@Validated @PathVariable long id, Principal principal) {
        return cardService.findById(id, principal);
    }

    @GetMapping(value = "/findByCardNumber/{cardNumber}", produces = "application/json")
    private Card findByCardNumber(@Validated @PathVariable String cardNumber, Principal principal) {
        return cardService.findByPan(cardNumber, principal);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Card update(@Validated @RequestBody Card card, Principal principal) {
        return cardService.update(card, principal);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id, Principal principal) {
        cardService.delete(id, principal);
    }
}
