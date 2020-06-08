package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.CardRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author adebola.owolabi
 */
@Service
public class CardService {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    public Page<Card> findAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return cardRepository.findAll(pageable);
    }

    public Card save(Card card) {
        long id = card.getId();
        boolean exists = cardRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Card already exists");
        return cardRepository.save(card);
    }

    public Card autoCreateForUser(User user){
        Card card = new Card();
        card.setPan(UUID.randomUUID().toString());
        card.setExpiry(LocalDate.now().plusYears(3));
        card.setOwner(user);
        card.setBalance(0);
        card.setEnabled(true);
        return cardRepository.save(card);
    }

    public Card findById(long id) {
        return cardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist"));
    }

    public Card findByPan(String cardNumber) {
        return cardRepository.findByPan(cardNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist"));
    }

    public Card findByOwner(long owner) {
        Optional<User> user = userRepository.findById(owner);
        if(user.isPresent())
            return cardRepository.findByOwner(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist"));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
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
            card.setEnabled(true);
            cardRepository.save(card);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist");
    }

    public void deactivate(long cardId) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if(cardOptional.isPresent()){
            Card card = cardOptional.get();
            card.setEnabled(false);
            cardRepository.save(card);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist");
    }

    public Long countAll(){
        return cardRepository.count();
    }

    public long countByOwner(User user){
        return cardRepository.countByOwner(user);
    }
}
