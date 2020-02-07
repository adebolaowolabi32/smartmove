package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.repository.ConfigRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigRepositoryTests {
    @Autowired
    ConfigRepository configRepository;

    @Test
    public void testFindById() {
        Config config = new Config();
        config.setConfigName("periodTransactionUpload");
        config.setConfigValue("90");
        Config savedConfig = configRepository.save(config);
        configRepository.findById(savedConfig.getConfigName()).ifPresent(config1 -> {
            assertThat(config1.getConfigName()).isEqualTo(config.getConfigName());
        });
    }

    @Test
    public void testFindAll() {
        Config config1 = new Config();
        config1.setConfigName("periodTransactionUpload");
        config1.setConfigValue("200");
        Config config2 = new Config();
        config2.setConfigName("periodGPS");
        config2.setConfigValue("90");
        configRepository.save(config1);
        configRepository.save(config2);
        Iterable<Config> configs = configRepository.findAll();
        Iterator<Config> configIterator = configs.iterator();

        int i = 0;
        while(configIterator.hasNext()) {
            i++;
            configIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);    }

    @Test
    public void testDelete() {
        Config config1 = new Config();
        config1.setConfigName("periodTransactionUpload");
        config1.setConfigValue("200");
        Config savedConfig = configRepository.save(config1);
        configRepository.deleteById(savedConfig.getConfigName());
        assertThat(configRepository.findById(savedConfig.getConfigName())).isEmpty();
    }
}
