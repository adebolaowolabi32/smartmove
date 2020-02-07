package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.model.SystemTimings;
import com.interswitch.smartmoveserver.model.request.GetSystemTimings;
import com.interswitch.smartmoveserver.model.response.GetSystemTimingsResponse;
import com.interswitch.smartmoveserver.repository.ConfigRepository;
import com.interswitch.smartmoveserver.service.SystemTimingsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemTimingsServiceTests {

    @Autowired
    private SystemTimingsService systemTimingsService;

    @MockBean
    private ConfigRepository configRepository;

    private Config config1;
    private Config config2;
    private SystemTimings systemTimings;
    private GetSystemTimings getTimings;
    private GetSystemTimingsResponse getTimingsResponse;

    @Before
    public void setup() {
        systemTimings = new SystemTimings();
        systemTimings.setPeriodGPS("12");
        systemTimings.setPeriodTransactionUpload("15");
        getTimings = new GetSystemTimings();
        getTimings.setDeviceId("id_beval");
        getTimings.setMessageId("id_message");
        getTimingsResponse = new GetSystemTimingsResponse();
        getTimingsResponse.setMessageId("id_message");
        getTimingsResponse.setPeriodGPS("12");
        getTimingsResponse.setPeriodTransactionUpload("15");
        getTimingsResponse.setResponseCode("00");
        config1 = new Config();
        config1.setConfigName("periodTransactionUpload");
        config1.setConfigValue(systemTimings.getPeriodTransactionUpload());
        config2 = new Config();
        config2.setConfigName("periodGPS");
        config2.setConfigValue(systemTimings.getPeriodGPS());
    }

    @Test
    public void testSaveTimings() {
        when(configRepository.save(config1)).thenReturn(config1);
        when(configRepository.save(config2)).thenReturn(config2);
        assertThat(systemTimingsService.saveSystemTimings(systemTimings).getPeriodGPS()).isEqualTo(systemTimings.getPeriodGPS());
    }

    @Test
    public void testGetSystemTimings() {
        when(configRepository.findAll()).thenReturn(Arrays.asList(config1, config2));
        assertThat(systemTimingsService.getSystemTimings(getTimings).getPeriodGPS()).isEqualTo(getTimingsResponse.getPeriodGPS());
    }
}
