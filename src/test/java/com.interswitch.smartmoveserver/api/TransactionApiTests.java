package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.request.LogTransaction;
import com.interswitch.smartmoveserver.model.response.LogTransactionResponse;
import com.interswitch.smartmoveserver.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = TransactionApi.class)
public class TransactionApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;

    private LogTransaction logTransaction;
    
    private LogTransactionResponse logTransactionResponse;

    private Transaction transaction;

    @Before
    public void setup() {
        transaction = new Transaction();
        long id = 1000013;
        transaction.setId(id);
        transaction.setTransactionId("");
        transaction.setDeviceId("123457385");
        transaction.setCardId("12345556");
        transaction.setType(Enum.TransactionType.CREDIT);
        transaction.setAmount(200.00);
        transaction.setAgentId("peace.miracle");
        transaction.setOperatorId("OP4564");
        transaction.setTerminalId("647593");
        transaction.setMode(Enum.TransportMode.RAIL);
        transaction.setTransactionDateTime(LocalDateTime.now());
    }

    @Test
    public void testSave() throws Exception {
        when(transactionService.saveTransaction(transaction)).thenReturn(transaction);
        mvc.perform(post("/transactions")
                .content(new ObjectMapper().writeValueAsString(transaction))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testGetAll() throws Exception {
        when(transactionService.getAll()).thenReturn(Arrays.asList(transaction, transaction));
        mvc.perform(get("/transactions")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetFindById() throws Exception {
        when(transactionService.findById(transaction.getId())).thenReturn(transaction);
        mvc.perform(get("/transactions/{id}", transaction.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

/*    @Test
    public void testFindByCardNumber() throws Exception {
        when(transactionService.findByCardNumber(transaction.getCardNumber())).thenReturn(Arrays.asList(transaction));
        mvc.perform(get("/transactions/{cardNumber}", transaction.getCardNumber())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testFindByDeviceId() throws Exception {
        when(transactionService.findByDeviceId(transaction.getDeviceId())).thenReturn(Arrays.asList(transaction));
        mvc.perform(get("/transactions/{deviceId}", transaction.getDeviceId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testFindBySender() throws Exception {
        when(transactionService.findBySender(transaction.getSender())).thenReturn(Arrays.asList(transaction));
        mvc.perform(get("/transactions/{sender}", transaction.getSender())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testFindByRecipient() throws Exception {
        when(transactionService.findByRecipient(transaction.getRecipient())).thenReturn(Arrays.asList(transaction));
        mvc.perform(get("/transactions/{recipient}", transaction.getRecipient())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }*/

}
