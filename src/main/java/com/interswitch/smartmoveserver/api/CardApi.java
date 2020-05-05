package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/cards")
public class CardApi {
    @Autowired
    CardService cardService;

    @GetMapping(produces = "application/json")
    private List<Card> getAll() {
        return cardService.getAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Card save(@Validated @RequestBody Card card) {
        return cardService.save(card);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Card findById(@Validated @PathVariable long id) {
        return cardService.findById(id);
    }

    @GetMapping(value = "/findByCardNumber/{cardNumber}", produces = "application/json")
    private Card findByCardNumber(@Validated @PathVariable String cardNumber) {
        return cardService.findByPan(cardNumber);
    }

    @PostMapping(value = "/{cardId}/assignToAgent/{agentId}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void assignToAgent(@Validated @PathVariable long cardId, @Validated @PathVariable long agentId) {
        cardService.assignToAgent(cardId, agentId);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Card update(@Validated @RequestBody Card card) {
        return cardService.update(card);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        cardService.delete(id);
    }

    @PostMapping(value = "/activate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void activate(@Validated @PathVariable long id) {
        cardService.activate(id);
    }

    @PostMapping(value = "/deactivate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void deactivate(@Validated @PathVariable long id) {
        cardService.deactivate(id);
    }
}
