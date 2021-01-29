package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import org.junit.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionRepositoryTests {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private Transaction transaction;

    private Transaction savedTransaction;

    @BeforeAll
    public void setUp() {
        transaction = new Transaction();
        //User user = userRepository.save(buildTestUser());
        transaction.setTransactionId("");
        transaction.setDeviceId("123457385");
        transaction.setCardId("12345556");
        transaction.setType(Enum.TransactionType.TAP_IN);
        transaction.setAmount(200.00);
        transaction.setOperatorId("OP4564");
        transaction.setTerminalId("647593");
        transaction.setMode(Enum.TransportMode.RAIL);
        transaction.setTransactionDateTime(LocalDateTime.now());
        savedTransaction = transactionRepository.save(transaction);
        assertNotNull(savedTransaction);
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId("23");
        transaction1.setDeviceId("123457385");
        transaction1.setCardId("12345556");
        transaction1.setType(Enum.TransactionType.TAP_OUT);
        transaction1.setAmount(200.00);
        transaction1.setOperatorId("OP4564");
        transaction1.setTerminalId("647593");
        transaction1.setMode(Enum.TransportMode.RAIL);
        transaction1.setTransactionDateTime(LocalDateTime.now());
        assertNotNull(transactionRepository.save(transaction1));
    }

    @After
    public void tearDown() {
        //delete all composite entities
        userRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    public void testFind() {
        List<Transaction> transactions = transactionRepository.findAllByType(savedTransaction.getType());
        assertTrue(transactions.size() >= 2);
    }

    @Test
    public void testFindById() {
        transactionRepository.findById(savedTransaction.getId()).ifPresent(transaction1 -> {
            assertEquals(transaction1.getDeviceId(), transaction.getDeviceId());
            assertEquals(transaction1.getType(), transaction.getType());
            assertEquals(transaction1.getAmount(), transaction.getAmount());
            assertEquals(transaction1.getCardId(), transaction.getCardId());
        });
    }

    @Test
    public void testFindByOperator() {
        List<Transaction> transactions = transactionRepository.findAllByOperatorId(savedTransaction.getOperatorId());
        assertTrue(transactions.size() >= 1);
    }

    @Test
    public void testFindAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertTrue(transactions.size() >= 2);
    }
}
