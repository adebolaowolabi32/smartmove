package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Iterator;

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
        transaction.setCardNumber("123456789123456");
        transaction.setSender(user);
        transaction.setRecipient(user);
        transaction.setTimeStamp(new Date());
        Transaction transaction1 = new Transaction();
        transaction1.setDevice(new Device());
        transaction1.setType(Enum.TransactionType.TRIP);
        transaction1.setAmount(300);
        transaction1.setCardNumber("123786789123456");
        transaction1.setSender(user);
        transaction1.setRecipient(user);
        transaction1.setTimeStamp(new Date());
        assertNotNull(transactionRepository.save(transaction1));
        savedTransaction = transactionRepository.save(transaction);
        assertNotNull(savedTransaction);
    }

    @Test
    public void testFind() {
        Iterable<Transaction> transactions = transactionRepository.findAllByType(savedTransaction.getType());
        Iterator<Transaction> transactionIterator = transactions.iterator();

        int i = 0;
        while(transactionIterator.hasNext()) {
            i++;
            transactionIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindById() {
        transactionRepository.findById(savedTransaction.getId()).ifPresent(transaction1 -> {
            assertThat(transaction1.getDevice()).isEqualTo(transaction.getDevice());
            assertThat(transaction1.getType()).isEqualTo(transaction.getType());
            assertThat(transaction1.getAmount()).isEqualTo(transaction.getAmount());
            assertThat(transaction1.getCardNumber()).isEqualTo(transaction.getCardNumber());
        });
    }

    @Test
    public void testFindBySender() {
        Iterable<Transaction> transactions = transactionRepository.findAllBySender(savedTransaction.getSender());
        Iterator<Transaction> transactionIterator = transactions.iterator();

        int i = 0;
        while(transactionIterator.hasNext()) {
            i++;
            transactionIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByRecipient() {
        Iterable<Transaction> transactions = transactionRepository.findAllByRecipient(savedTransaction.getRecipient());
        Iterator<Transaction> transactionIterator = transactions.iterator();

        int i = 0;
        while(transactionIterator.hasNext()) {
            i++;
            transactionIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByCardNumber() {
        Iterable<Transaction> transactions = transactionRepository.findAllByCardNumber(savedTransaction.getCardNumber());
        Iterator<Transaction> transactionIterator = transactions.iterator();

        int i = 0;
        while(transactionIterator.hasNext()) {
            i++;
            transactionIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testFindByDeviceId() {
        Iterable<Transaction> transactions = transactionRepository.findAllByDevice(savedTransaction.getDevice());
        Iterator<Transaction> transactionIterator = transactions.iterator();

        int i = 0;
        while(transactionIterator.hasNext()) {
            i++;
            transactionIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testFindAll() {
        Iterable<Transaction> transactions = transactionRepository.findAll();
        Iterator<Transaction> transactionIterator = transactions.iterator();

        int i = 0;
        while(transactionIterator.hasNext()) {
            i++;
            transactionIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);
    }

    @After
    public void testDelete() {
        transactionRepository.deleteAll();
        assertEquals(transactionRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
