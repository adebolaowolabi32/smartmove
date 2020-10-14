package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.model.Enum;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigRepositoryTests {
    @Autowired
    private ConfigRepository configRepository;

    private Config config;
    private Config savedConfig;

    @BeforeAll
    public void setUp() {
        config = new Config();
        config.setName(Enum.ConfigList.TRANSACTION_UPLOAD_PERIOD);
        config.setValue("200");
        Config config2 = new Config();
        config2.setName(Enum.ConfigList.GPS_UPLOAD_PERIOD);
        config2.setValue("90");
        assertNotNull(configRepository.save(config2));
        savedConfig = configRepository.save(config);
        assertNotNull(savedConfig);
    }

    @Test
    public void testFindById() {
        configRepository.findById(savedConfig.getId()).ifPresent(config1 -> {
            assertEquals(config1.getName(), config.getName());
            assertEquals(config1.getValue(), config.getValue());
        });
    }

    @Test
    public void testFindByName() {
        configRepository.findByName(savedConfig.getName()).ifPresent(config1 -> {
            assertEquals(config1.getId(), config.getId());
            assertEquals(config1.getValue(), config.getValue());
        });
    }

    @Test
    public void testFindAll() {
        List<Config> configs = configRepository.findAll();
        assertTrue(configs.size() >= 2);
    }

    @AfterAll
    public void testDelete() {
        configRepository.deleteAll();
        assertEquals(configRepository.findAll().iterator().hasNext(), false);
    }
}
