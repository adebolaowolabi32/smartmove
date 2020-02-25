package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.CardRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CardService {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    public Iterable<Card> getAll() {
        return cardRepository.findAll();
    }

    public Card save(Card card) {
        long id = card.getId();
        boolean exists = cardRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Card already exists");
        return cardRepository.save(card);
    }

    public Card findById(long id) {
        return cardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist"));
    }

    public Card findByPan(String cardNumber) {
        return cardRepository.findByPan(cardNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist"));
    }

    public Card update(Card card) {
        Optional<Card> existing = cardRepository.findById(card.getId());
        if(existing.isPresent())
            return cardRepository.save(card);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist");
    }

    public void assignToAgent(long cardId, long agentId) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if(cardOptional.isPresent()){
            Optional<User> userOptional = userRepository.findById(agentId);
            if(userOptional.isPresent()){
                Card card = cardOptional.get();
                card.setOwner(userOptional.get());
                cardRepository.save(card);
            }
            else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Agent does not exist");
        }
        else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Card does not exist");
    }


    public void delete(long id) {
        Optional<Card> existing = cardRepository.findById(id);
        if(existing.isPresent())
            cardRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist");
        }
    }

    public void activate(long cardId) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if(cardOptional.isPresent()){
            Card card = cardOptional.get();
            card.setActive(true);
            cardRepository.save(card);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist");
    }

    public void deactivate(long cardId) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if(cardOptional.isPresent()){
            Card card = cardOptional.get();
            card.setActive(false);
            cardRepository.save(card);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist");
    }
}
