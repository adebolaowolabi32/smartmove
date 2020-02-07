package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.model.SystemTimings;
import com.interswitch.smartmoveserver.model.request.GetSystemTimings;
import com.interswitch.smartmoveserver.model.response.GetSystemTimingsResponse;
import com.interswitch.smartmoveserver.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SystemTimingsService {
    @Autowired
    private ConfigRepository configRepository;

    public GetSystemTimingsResponse getSystemTimings(GetSystemTimings getSystemTimings) {
        GetSystemTimingsResponse getSystemTimingsResponse = new GetSystemTimingsResponse();
        configRepository.findAll().forEach(config -> {
            switch (config.getConfigName()) {
                case "periodTransactionUpload":
                    getSystemTimingsResponse.setPeriodTransactionUpload(config.getConfigValue());
                    break;
                case "periodGPS":
                    getSystemTimingsResponse.setPeriodGPS(config.getConfigValue());
                    break;
                default:
                    break;
            }
        });
        getSystemTimingsResponse.setMessageId(getSystemTimings.getMessageId());
        getSystemTimingsResponse.setResponseCode("00");
        return getSystemTimingsResponse;
    }

    public SystemTimings saveSystemTimings(SystemTimings systemTimings) {
        Config config1 = new Config();
        config1.setConfigName("periodTransactionUpload");
        config1.setConfigValue(systemTimings.getPeriodTransactionUpload());
        Config config2 = new Config();
        config2.setConfigName("periodGPS");
        config2.setConfigValue(systemTimings.getPeriodGPS());
        configRepository.saveAll(Arrays.asList(config1, config2));
        return systemTimings;
    }
}
