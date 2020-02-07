package com.interswitch.smartmoveserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.request.LogTransaction;
import com.interswitch.smartmoveserver.model.response.LogTransactionResponse;
import com.interswitch.smartmoveserver.service.TransactionService;
import com.interswitch.smartmoveserver.controller.TransactionController;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = TransactionController.class)
public class TransactionLogsControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;

    private LogTransaction logTransaction;
    private LogTransactionResponse logTransactionResponse;

    @Before
    public void setup() {
        logTransaction = new LogTransaction();
        logTransaction.setDeviceId("id_beval");
        logTransaction.setMessageId("id_message");
        logTransaction.setAmount("200");
        logTransaction.setCardNumber("0123456789");
        logTransaction.setTimeDate(LocalDateTime.now().toString());
        logTransaction.setType(1);
        logTransactionResponse = new LogTransactionResponse();
        logTransactionResponse.setMessageId("id_message");
        logTransactionResponse.setResponseCode("00");
    }

    @Test
    public void testSaveTransaction() throws Exception {
       when(transactionService.saveTransaction(logTransaction)).thenReturn(logTransactionResponse);

        mvc.perform(post("/transaction")
                .content(new ObjectMapper().writeValueAsString(logTransaction))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(logTransactionResponse)));
    }
}
