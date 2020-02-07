package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.request.LogTransaction;
import com.interswitch.smartmoveserver.model.response.LogTransactionResponse;
import com.interswitch.smartmoveserver.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public LogTransactionResponse saveTransaction(LogTransaction logTransaction) {
        Transaction transaction = new Transaction();
        transaction.setDeviceId(logTransaction.getDeviceId());
        transaction.setCardNumber(logTransaction.getCardNumber());
        transaction.setAmount(logTransaction.getAmount());
        transaction.setType(logTransaction.getType());
        transaction.setTimeDate(logTransaction.getTimeDate());
        transactionRepository.save(transaction);
        LogTransactionResponse logTransactionResponse = new LogTransactionResponse();
        logTransactionResponse.setMessageId(logTransaction.getMessageId());
        logTransactionResponse.setResponseCode("13");
        return logTransactionResponse;
    }
}
