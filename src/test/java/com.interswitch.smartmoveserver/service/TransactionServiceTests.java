package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Card;
import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.request.LogTransaction;
import com.interswitch.smartmoveserver.model.response.LogTransactionResponse;
import com.interswitch.smartmoveserver.repository.TransactionRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

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
        transaction.setDevice(new Device());
        transaction.setAmount(200.00);
        transaction.setCard(new Card());
        transaction.setTimeStamp(LocalDateTime.now());
        transaction.setType(Enum.TransactionType.TRIP);
        logTransaction = new LogTransaction();
        logTransaction.setDevice(new Device());
        logTransaction.setMessageId("id_message");
        logTransaction.setAmount(20.00);
        logTransaction.setCard(new Card());
        logTransaction.setTimeStamp(LocalDateTime.now());
        logTransaction.setType(Enum.TransactionType.CREDIT);
        logTransactionResponse = new LogTransactionResponse();
        logTransactionResponse.setMessageId("id_message");
        logTransactionResponse.setResponseCode("00");
    }
}
