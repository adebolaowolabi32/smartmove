package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceRepositoryTests {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    private Device device;
    private Device savedDevice;

    @Before
    public void setUp() {
        device = new Device();
        device.setName("agent_device_A");
        device.setDeviceId("012345");
        User user = buildTestUser();
        userRepository.save(user);
        device.setOwner(user);
        device.setType(Enum.DeviceType.READER);
        device.setDeviceStatus(Enum.DeviceStatus.DISCONNECTED);
        device.setHardwareVersion("1.0.0");
        device.setSoftwareVersion("2.0.2");
        device.setFareType(Enum.FareType.FIXED);
        Device device1 = new Device();
        device1.setName("agent_device_B");
        device1.setDeviceId("012346");
        device1.setOwner(user);
        device1.setType(Enum.DeviceType.VALIDATOR);
        device1.setDeviceStatus(Enum.DeviceStatus.CONNECTED);
        device1.setHardwareVersion("1.0.0");
        device1.setSoftwareVersion("2.0.2");
        device1.setFareType(Enum.FareType.VARIABLE);
        assertNotNull(deviceRepository.save(device1));
        savedDevice = deviceRepository.save(device);
        assertNotNull(savedDevice);
    }

    @Test
    public void testFindById() {
        deviceRepository.findById(savedDevice.getId()).ifPresent(device1 -> {
            assertThat(device1.getOwner()).isEqualTo(device.getOwner());
            assertThat(device1.getType()).isEqualTo(device.getType());
            assertThat(device1.getDeviceStatus()).isEqualTo(device.getDeviceStatus());
            assertThat(device1.getHardwareVersion()).isEqualTo(device.getHardwareVersion());
            assertThat(device1.getSoftwareVersion()).isEqualTo(device.getSoftwareVersion());
            assertThat(device1.getFareType()).isEqualTo(device.getFareType());

        });
    }

    @Test
    public void testFindAll() {
        Iterable<Device> devices = deviceRepository.findAll();
        Iterator<Device> deviceIterator = devices.iterator();

        int i = 0;
        while(deviceIterator.hasNext()) {
            i++;
            deviceIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByOwner() {
        Iterable<Device> devices = deviceRepository.findAllByOwner(device.getOwner());
        Iterator<Device> deviceIterator = devices.iterator();

        int i = 0;
        while(deviceIterator.hasNext()) {
            i++;
            deviceIterator.next();
        }
        assertThat(i).isGreaterThanOrEqualTo(1);
    }

    @After
    public void testDelete() {
        deviceRepository.deleteAll();
        assertEquals(deviceRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}