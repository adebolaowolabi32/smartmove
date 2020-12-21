package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.dto.CardDto;
import com.interswitch.smartmoveserver.repository.CardRepository;
import com.interswitch.smartmoveserver.util.DateUtil;
import com.interswitch.smartmoveserver.util.FileParser;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
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
    UserService userService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public List<Card> findAll(Long owner, String principal) {
        User user = userService.findByUsername(principal);

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                return cardRepository.findAllByOwner(user);
            } else {
                return cardRepository.findAll();
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                return cardRepository.findAllByOwner(ownerUser);
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public PageView<Card> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<Card> pages = cardRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Card> pages = cardRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<Card> pages = cardRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());

            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Card save(Card card, String principal) {
        String pan = card.getPan();
        boolean exists = cardRepository.existsByPan(pan);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "A card with this PAN: " + pan + " already exists.");
        if(card.getOwner() == null) {
            User owner = userService.findByUsername(principal);
            card.setOwner(owner);
        }
        return cardRepository.save(card);
    }

    public Card autoCreateForUser(User user){
        Card card = new Card();
        card.setPan(UUID.randomUUID().toString());
        card.setExpiry(LocalDate.now().plusYears(3));
        card.setOwner(user);
        card.setType(Enum.CardType.AGENT);
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

    public Card findByOwner(String owner) {
        User user = userService.findByUsername(owner);
        if (user != null)
            return cardRepository.findByOwner(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist"));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Card update(Card card, String principal) {
        Optional<Card> existing = cardRepository.findById(card.getId());
        if(existing.isPresent()){
            String pan = card.getPan();
            boolean exists = cardRepository.existsByPan(pan);
            if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "A card with this PAN: " + pan + " already exists.");
            if(card.getOwner() == null) {
                User owner = userService.findByUsername(principal);
                card.setOwner(owner);
            }
            return cardRepository.save(card);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card does not exist");
    }


    @Audited(auditableAction = AuditableAction.DELETE, auditableActionClass = AuditableActionStatusImpl.class)
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

    public Long countByOwner(String username) {
        User user = userService.findByUsername(username);
        return cardRepository.countByOwner(user);
    }

    public boolean upload(MultipartFile file, String principal) throws IOException {
        User owner = userService.findByUsername(principal);
        List<Card> savedCards = new ArrayList<>();
        if(file.getSize()>1){
            FileParser<CardDto> fileParser = new FileParser<>();
            List<CardDto> cardDtoList = fileParser.parseFileToEntity(file, CardDto.class);
            cardDtoList.forEach(cardDto->{
                savedCards.add(cardRepository.save(mapToCard(cardDto, owner)));
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
        return enabledStatus.equalsIgnoreCase("true") || enabledStatus.startsWith("true");
    }
}
