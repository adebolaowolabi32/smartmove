package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Wallet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletRepositoryTests {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;
    private Wallet wallet;
    private Wallet savedWallet;

    @Before
    public void setUp() {
        wallet = new Wallet();
        wallet.setCurrency("NGN");
        User user = buildTestUser();
        userRepository.save(user);
        wallet.setOwner(user);
        wallet.setBalance(200000);
        wallet.setActive(true);
        Wallet wallet1 = new Wallet();
        wallet1.setCurrency("USD");
        wallet1.setOwner(user);
        wallet1.setBalance(2000);
        wallet1.setActive(true);
        assertNotNull(walletRepository.save(wallet1));
        savedWallet = walletRepository.save(wallet);
        assertNotNull(savedWallet);
    }

    @Test
    public void testFindById() {
        walletRepository.findById(savedWallet.getId()).ifPresent(wallet1 -> {
            assertThat(wallet1.getBalance()).isEqualTo(wallet1.getBalance());
            assertThat(wallet1.getOwner()).isEqualTo(wallet1.getOwner());
            assertThat(wallet1.getCurrency()).isEqualTo(wallet1.getCurrency());
            assertThat(wallet1.isActive()).isEqualTo(wallet1.isActive());
        });
    }

    @After
    public void testDelete() {
        walletRepository.deleteAll();
        assertEquals(walletRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}
