package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTests {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private Transaction transaction;

    private Transaction savedTransaction;

    @Before
    public void setUp() {
        transaction = new Transaction();
        User user = buildTestUser();
        userRepository.save(user);
        transaction.setTransactionId("");
        transaction.setDeviceId("123457385");
        transaction.setCardId("12345556");
        transaction.setType(Enum.TransactionType.TRIP);
        transaction.setAmount(200.00);
        transaction.setAgentId("peace.miracle");
        transaction.setOperatorId("OP4564");
        transaction.setTerminalId("647593");
        transaction.setMode(Enum.TransportMode.RAIL);
        transaction.setTransactionDateTime(LocalDateTime.now());
        savedTransaction = transactionRepository.save(transaction);
        assertNotNull(savedTransaction);
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId("");
        transaction1.setDeviceId("123457385");
        transaction1.setCardId("12345556");
        transaction1.setType(Enum.TransactionType.TRIP);
        transaction1.setAmount(200.00);
        transaction1.setAgentId("peace.miracle");
        transaction1.setOperatorId("OP4564");
        transaction1.setTerminalId("647593");
        transaction1.setMode(Enum.TransportMode.RAIL);
        transaction1.setTransactionDateTime(LocalDateTime.now());
        assertNotNull(transactionRepository.save(transaction1));
    }

    @Test
    public void testFind() {
        List<Transaction> transactions = transactionRepository.findAllByType(savedTransaction.getType());
        assertThat(transactions.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindById() {
        transactionRepository.findById(savedTransaction.getId()).ifPresent(transaction1 -> {
            assertThat(transaction1.getDeviceId()).isEqualTo(transaction.getDeviceId());
            assertThat(transaction1.getType()).isEqualTo(transaction.getType());
            assertThat(transaction1.getAmount()).isEqualTo(transaction.getAmount());
            assertThat(transaction1.getCardId()).isEqualTo(transaction.getCardId());
        });
    }

    @Test
    public void testFindByOperator() {
        List<Transaction> transactions = transactionRepository.findAllByOperatorId(savedTransaction.getOperatorId());
        assertThat(transactions.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testFindByAgent() {
        List<Transaction> transactions = transactionRepository.findAllByAgentId(savedTransaction.getAgentId());
        assertThat(transactions.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testFindAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions.size()).isGreaterThanOrEqualTo(2);
    }
}
