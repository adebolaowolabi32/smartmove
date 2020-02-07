package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.request.LogTransaction;
import com.interswitch.smartmoveserver.model.response.LogTransactionResponse;
import com.interswitch.smartmoveserver.repository.TransactionRepository;
import com.interswitch.smartmoveserver.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTests {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    private Transaction transaction;
    private LogTransaction logTransaction;
    private LogTransactionResponse logTransactionResponse;

    @Before
    public void setup() {
        transaction = new Transaction();
        transaction.setDeviceId("id_beval");
        transaction.setAmount("200");
        transaction.setCardNumber("0123456789");
        transaction.setTimeDate(LocalDateTime.now().toString());
        transaction.setType(0);
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
    public void testSaveTransaction() {
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        assertThat(transactionService.saveTransaction(logTransaction).getMessageId()).isEqualTo(logTransactionResponse.getMessageId());
    }
}
