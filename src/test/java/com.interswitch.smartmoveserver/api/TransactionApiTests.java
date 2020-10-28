package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.service.TransactionService;
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

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = TransactionApi.class)
public class TransactionApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;

    private Transaction transaction;

    @BeforeAll
    public void setup() {
        transaction = new Transaction();
        long id = 1000013;
        transaction.setId(id);
        transaction.setTransactionId("");
        transaction.setDeviceId("123457385");
        transaction.setCardId("12345556");
        transaction.setType(Enum.TransactionType.CREDIT);
        transaction.setAmount(200.00);
        transaction.setOperatorId("OP4564");
        transaction.setTerminalId("647593");
        transaction.setMode(Enum.TransportMode.RAIL);
        transaction.setTransactionDateTime(LocalDateTime.now());
    }

    @Test
    public void testSave() throws Exception {
        when(transactionService.save(transaction, "")).thenReturn(transaction);
        mvc.perform(post("/api/transactions")
                .content(new ObjectMapper().writeValueAsString(transaction))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testfindAll() throws Exception {
        when(transactionService.findAll()).thenReturn(Arrays.asList(transaction, transaction));
        mvc.perform(get("/api/transactions")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetFindById() throws Exception {
        when(transactionService.findById(transaction.getId(), "")).thenReturn(transaction);
        mvc.perform(get("/api/transactions/{id}", transaction.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }
}
