package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class CardRepositoryTests {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    private Card card;

    private Card savedCard;

    private long id;

    @BeforeAll
    public void setUp() {
        card = new Card();
        card.setPan("0123456789012345");
        User user = buildTestUser();
        userRepository.save(user);
        card.setOwner(user);
        card.setType(Enum.CardType.AGENT);
        card.setExpiry(LocalDate.now().plusYears(3));
        card.setBalance(10000);
        card.setEnabled(false);
        Card card1 = new Card();
        card1.setPan("0123458789012345");
        card1.setOwner(user);
        card1.setType(Enum.CardType.EMV);
        card.setExpiry(LocalDate.now().plusYears(3));
        card1.setBalance(32000000);
        card1.setEnabled(true);
        assertNotNull(cardRepository.save(card1));
        savedCard = cardRepository.save(card);
        id = savedCard.getId();
        assertNotNull(savedCard);
    }

    @Test
    public void testFindById() {
        cardRepository.findById(savedCard.getId()).ifPresent(card1 -> {
            assertEquals(card1.getOwner(), card.getOwner());
            assertEquals(card1.getType(), card.getType());
            assertEquals(card1.getBalance(), card.getBalance());
            assertEquals(card1.isEnabled(), card.isEnabled());
        });
    }

    @Test
    public void testFindByPan() {
        cardRepository.findByPan(savedCard.getPan()).ifPresent(card1 -> {
            assertEquals(card1.getOwner(), card.getOwner());
            assertEquals(card1.getType(), card.getType());
            assertEquals(card1.getBalance(), card.getBalance());
            assertEquals(card1.isEnabled(), card.isEnabled());
        });
    }

    @Test
    public void testFindAll() {
        List<Card> cards = cardRepository.findAll();
        assertEquals(cards.size(), 2);
    }

    @AfterAll
    public void testDelete() {
        cardRepository.deleteById(id);
        assertNull(cardRepository.findById(id));
    }
}
