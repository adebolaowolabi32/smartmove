package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.DeviceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceServiceTests {
    @Autowired
    private DeviceService deviceService;
    @MockBean
    private DeviceRepository deviceRepository;

    private Device device;

    @Before
    public void setup() {
        device = new Device();
        device.setOwner(new User());
        device.setDeviceId("id_device");
    }

}
