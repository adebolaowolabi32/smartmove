package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.CardService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = CardApi.class)
public class CardApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CardService cardService;

    private Card card;

    @BeforeAll
    public void setup() {
        card = new Card();
        long id = 1000013;
        card.setId(id);
        card.setPan("12345678901234");
        card.setBalance(40000);
        card.setOwner(new User());
        card.setExpiry(LocalDate.now().plusYears(3));
        card.setType(Enum.CardType.AGENT);
        card.setEnabled(true);
    }

    @Test
    public void testSave() throws Exception {
        when(cardService.save(card, any(Principal.class))).thenReturn(card);
        mvc.perform(post("/api/cards")
                .content(new ObjectMapper().writeValueAsString(card))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(cardService.update(card, any(Principal.class))).thenReturn(card);
        mvc.perform(put("/api/cards")
                .content(new ObjectMapper().writeValueAsString(card))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testfindAll() throws Exception {
        when(cardService.findAll()).thenReturn(Arrays.asList(card, new Card()));
        mvc.perform(get("/api/cards")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetFindById() throws Exception {
        when(cardService.findById(card.getId(), any(Principal.class))).thenReturn(card);
        mvc.perform(get("/api/cards/{id}", card.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

/*    @Test
    public void testGetFindByPan() throws Exception {
        when(cardService.findByPan(card.getPan())).thenReturn(card);
        mvc.perform(get("/cards/{cardNumber}", card.getPan())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(card)));
    }*/

    @AfterAll
    public void testDelete() throws Exception {
        mvc.perform(delete("/api/cards/{id}", card.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
