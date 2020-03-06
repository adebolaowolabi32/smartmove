package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
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
        transaction.setDevice(new Device());
        transaction.setType(Enum.TransactionType.TRIP);
        transaction.setAmount(200.00);
        transaction.setCard(new Card());
        transaction.setSender(user);
        transaction.setRecipient(user);
        transaction.setTimeStamp(new Date());
        Transaction transaction1 = new Transaction();
        transaction1.setDevice(new Device());
        transaction1.setType(Enum.TransactionType.TRIP);
        transaction1.setAmount(300);
        transaction1.setCard(new Card());
        transaction1.setSender(user);
        transaction1.setRecipient(user);
        transaction1.setTimeStamp(new Date());
        assertNotNull(transactionRepository.save(transaction1));
        savedTransaction = transactionRepository.save(transaction);
        assertNotNull(savedTransaction);
    }

    @Test
    public void testFind() {
        List<Transaction> transactions = transactionRepository.findAllByType(savedTransaction.getType());
        assertThat(transactions.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindById() {
        transactionRepository.findById(savedTransaction.getId()).ifPresent(transaction1 -> {
            assertThat(transaction1.getDevice()).isEqualTo(transaction.getDevice());
            assertThat(transaction1.getType()).isEqualTo(transaction.getType());
            assertThat(transaction1.getAmount()).isEqualTo(transaction.getAmount());
            assertThat(transaction1.getCard()).isEqualTo(transaction.getCard());
        });
    }

    @Test
    public void testFindBySender() {
        List<Transaction> transactions = transactionRepository.findAllBySender(savedTransaction.getSender());
        assertThat(transactions.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByRecipient() {
        List<Transaction> transactions = transactionRepository.findAllByRecipient(savedTransaction.getRecipient());
        assertThat(transactions.size()).isGreaterThanOrEqualTo(2);
    }

   /* @Test
    public void testFindByCardNumber() {
        List<Transaction> transactions = transactionRepository.findAllByCard(savedTransaction.getCard());
        assertThat(transactions.size()).isGreaterThanOrEqualTo(1);
    }*/

    @Test
    public void testFindByDeviceId() {
        List<Transaction> transactions = transactionRepository.findAllByDevice(savedTransaction.getDevice());
        assertThat(transactions.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testFindAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions.size()).isGreaterThanOrEqualTo(2);
    }

    @After
    public void testDelete() {
        transactionRepository.deleteAll();
        assertEquals(transactionRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
