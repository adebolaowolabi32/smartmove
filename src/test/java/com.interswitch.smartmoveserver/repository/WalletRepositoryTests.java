package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WalletRepositoryTests {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;
    private Wallet wallet;
    private Wallet savedWallet;

    @BeforeAll
    public void setUp() {
        wallet = new Wallet();
        User user = buildTestUser();
        userRepository.save(user);
        wallet.setOwner(user);
        wallet.setBalance(200000);
        wallet.setEnabled(true);
        Wallet wallet1 = new Wallet();
        wallet1.setOwner(user);
        wallet1.setBalance(2000);
        wallet1.setEnabled(true);
        assertNotNull(walletRepository.save(wallet1));
        savedWallet = walletRepository.save(wallet);
        assertNotNull(savedWallet);
    }

    @Test
    public void testFindById() {
        walletRepository.findById(savedWallet.getId()).ifPresent(wallet1 -> {
            assertEquals(wallet1.getBalance(), wallet1.getBalance());
            assertEquals(wallet1.getOwner(), wallet1.getOwner());
            assertEquals(wallet1.isEnabled(), wallet1.isEnabled());
        });
    }

    @AfterAll
    public void testDelete() {
        walletRepository.deleteAll();
        assertEquals(walletRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
