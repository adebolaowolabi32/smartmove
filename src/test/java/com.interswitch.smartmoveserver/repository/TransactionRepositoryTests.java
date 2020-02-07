package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Transaction;
import com.interswitch.smartmoveserver.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTests {
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void testSaveAndFindById() {
        Transaction transaction = new Transaction();
        transaction.setId("test_transaction");
        transaction.setDeviceId("12");
        transaction.setType(0);
        transaction.setAmount("0000002");
        transaction.setCardNumber("0000002");
        transaction.setTimeDate("0000002");

        Transaction savedTransaction = transactionRepository.save(transaction);
        transactionRepository.findById(savedTransaction.getId()).ifPresent(transaction1 -> {
            assertThat(transaction1.getDeviceId()).isEqualTo(transaction.getDeviceId());
            assertThat(transaction1.getType()).isEqualTo(transaction.getType());
            assertThat(transaction1.getAmount()).isEqualTo(transaction.getAmount());
            assertThat(transaction1.getCardNumber()).isEqualTo(transaction.getCardNumber());
            assertThat(transaction1.getTimeDate()).isEqualTo(transaction.getTimeDate());

        });
    }

    @Test
    public void testFindAll() {
        Transaction transaction = new Transaction();
        transaction.setId("test_transaction");
        transaction.setDeviceId("A012342");
        transaction.setType(0);
        transaction.setAmount("200");
        transaction.setCardNumber("123456789123456");
        transaction.setTimeDate("4th Dec, 2019");
        Transaction transaction1 = new Transaction();
        transaction1.setId("test_transaction1");
        transaction1.setDeviceId("B0287363");
        transaction1.setType(0);
        transaction1.setAmount("300");
        transaction1.setCardNumber("123786789123456");
        transaction1.setTimeDate("6th Jan, 2019");
        transactionRepository.save(transaction);
        transactionRepository.save(transaction1);
        Iterable<Transaction> transactions = transactionRepository.findAll();
        Iterator<Transaction> transactionIterator = transactions.iterator();

        int i = 0;
        while(transactionIterator.hasNext()) {
            i++;
            transactionIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testDelete() {
        Transaction transaction = new Transaction();
        transaction.setId("test_transaction");
        transaction.setDeviceId("A012342");
        transaction.setType(0);
        transaction.setAmount("200");
        transaction.setCardNumber("123456789123456");
        transaction.setTimeDate("4th Dec, 2019");
        Transaction savedTransaction = transactionRepository.save(transaction);
        transactionRepository.deleteById(savedTransaction.getId());
        assertThat(transactionRepository.findById(savedTransaction.getId())).isEmpty();
    }
}
