package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.model.Enum;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigRepositoryTests {
    @Autowired
    private ConfigRepository configRepository;

    private Config config;
    private Config savedConfig;

    @Before
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
            assertThat(config1.getName()).isEqualTo(config.getName());
            assertThat(config1.getValue()).isEqualTo(config.getValue());
        });
    }

    @Test
    public void testFindByName() {
        configRepository.findByName(savedConfig.getName()).ifPresent(config1 -> {
            assertThat(config1.getId()).isEqualTo(config.getId());
            assertThat(config1.getValue()).isEqualTo(config.getValue());
        });
    }

    @Test
    public void testFindAll() {
        Iterable<Config> configs = configRepository.findAll();
        Iterator<Config> configIterator = configs.iterator();

        int i = 0;
        while(configIterator.hasNext()) {
            i++;
            configIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);    }

    @After
    public void testDelete() {
        configRepository.deleteAll();
        assertEquals(configRepository.findAll().iterator().hasNext(), false);
    }
}
