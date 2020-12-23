package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.service.CardService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/cards")
public class CardApi {
    @Autowired
    CardService cardService;

    @GetMapping(produces = "application/json")
    private PageView<Card> findAll(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return cardService.findAllPaginated(0L, page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Card save(@Validated @RequestBody Card card) {
        return cardService.save(card, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Card findById(@Validated @PathVariable long id) {
        return cardService.findById(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/findByCardNumber/{cardNumber}", produces = "application/json")
    private Card findByCardNumber(@Validated @PathVariable String cardNumber) {
        return cardService.findByPan(cardNumber, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Card update(@Validated @RequestBody Card card) {
        return cardService.update(card, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        cardService.delete(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}
