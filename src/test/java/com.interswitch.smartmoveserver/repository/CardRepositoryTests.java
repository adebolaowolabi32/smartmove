package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CardRepositoryTests {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    private Card card;

    private Card savedCard;

    @Before
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
        assertNotNull(savedCard);
    }

    @Test
    public void testFindById() {
        cardRepository.findById(savedCard.getId()).ifPresent(card1 -> {
            assertThat(card1.getOwner()).isEqualTo(card.getOwner());
            assertThat(card1.getType()).isEqualTo(card.getType());
            assertThat(card1.getBalance()).isEqualTo(card.getBalance());
            assertThat(card1.isEnabled()).isEqualTo(card.isEnabled());
        });
    }

    @Test
    public void testFindByPan() {
        cardRepository.findByPan(savedCard.getPan()).ifPresent(card1 -> {
            assertThat(card1.getOwner()).isEqualTo(card.getOwner());
            assertThat(card1.getType()).isEqualTo(card.getType());
            assertThat(card1.getBalance()).isEqualTo(card.getBalance());
            assertThat(card1.isEnabled()).isEqualTo(card.isEnabled());
        });
    }

    @Test
    public void testFindAll() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards.size()).isEqualTo(2);
    }

    @After
    public void testDelete() {
        cardRepository.deleteAll();
        assertEquals(cardRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
