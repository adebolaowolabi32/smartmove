package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.dto.CardDto;
import com.interswitch.smartmoveserver.model.dto.TripDto;
import com.interswitch.smartmoveserver.repository.CardRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public PageView<Card> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole())) {
                Page<Card> pages = cardRepository.findAllByOwner(pageable, user.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Card> pages = cardRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User> ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                Page<Card> pages = cardRepository.findAllByOwner(pageable, ownerUser.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());

            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public Card save(Card card, String principal) {
        String pan = card.getPan();
        boolean exists = cardRepository.existsByPan(pan);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "A card with this PAN: " + pan + " already exists.");
        if(card.getOwner() == null) {
            User owner = userRepository.findByUsername(principal).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
            card.setOwner(owner);
        }
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

    public Card findById(long id, String principal) {
        return cardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist"));
    }

    public Card findByPan(String cardNumber, String principal) {
        return cardRepository.findByPan(cardNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist"));
    }

    public Card findByOwner(long owner) {
        Optional<User> user = userRepository.findById(owner);
        if(user.isPresent())
            return cardRepository.findByOwner(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist"));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public Card update(Card card, String principal) {
        Optional<Card> existing = cardRepository.findById(card.getId());
        if(existing.isPresent()){
            String pan = card.getPan();
            boolean exists = cardRepository.existsByPan(pan);
            if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "A card with this PAN: " + pan + " already exists.");
            if(card.getOwner() == null) {
                User owner = userRepository.findByUsername(principal).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
                card.setOwner(owner);
            }
            return cardRepository.save(card);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist");
    }

    public void delete(long id, String principal) {
        Optional<Card> existing = cardRepository.findById(id);
        if(existing.isPresent())
            cardRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist");
        }
    }

    public Long countAll(){
        return cardRepository.count();
    }

    public long countByOwner(User user){
        return cardRepository.countByOwner(user);
    }

    public boolean upload(MultipartFile file, String principal) throws IOException {
        Optional<User> ownerOptional = userRepository.findByUsername(principal);
        List<Card> savedCards = new ArrayList<>();
        if(file.getSize()>1){
            FileParser<CardDto> fileParser = new FileParser<>();
            List<CardDto> cardDtoList = fileParser.parseFileToEntity(file, CardDto.class);
            cardDtoList.forEach(cardDto->{
                savedCards.add(cardRepository.save(mapToCard(cardDto, ownerOptional.isPresent() ? ownerOptional.get() : null)));
            });
        }
        return savedCards.size()>1;
    }

    private Card mapToCard(CardDto cardDto, User owner){

        return Card.builder()
                .balance(0)
                .enabled(true)
                .expiry(DateUtil.textToLocalDate(cardDto.getExpiry()))
                .pan(cardDto.getPan())
                .type(convertToCardTypeEnum(cardDto.getType()))
                .enabled(isEnabled(cardDto.getEnabled()))
                .owner(owner)
                .build();
    }

    private Enum.CardType convertToCardTypeEnum(String mode){
        //ISW_ADMIN, REGULATOR, OPERATOR, VEHICLE_OWNER, EMV, DRIVER, AGENT, COMMUTER
        return Enum.CardType.valueOf(mode.toUpperCase());
    }

    private boolean isEnabled(String enabledStatus){
        if(enabledStatus.equalsIgnoreCase("true") || enabledStatus.startsWith("true")){
            return true;
        }
        return false;
    }
}
