package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.DeviceService;
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

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DeviceService.class})
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceRepositoryTests {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    private Device device;
    private Device savedDevice;

    @BeforeAll
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
            assertEquals(device1.getOwner(), device.getOwner());
            assertEquals(device1.getType(), device.getType());
            assertEquals(device1.getDeviceStatus(), device.getDeviceStatus());
            assertEquals(device1.getHardwareVersion(), device.getHardwareVersion());
            assertEquals(device1.getSoftwareVersion(), device.getSoftwareVersion());
            assertEquals(device1.getFareType(), device.getFareType());

        });
    }

    @Test
    public void testFindAll() {
        List<Device> devices = deviceRepository.findAll();
        assertTrue(devices.size() >= 2);
    }

    @Test
    public void testFindByOwner() {
        List<Device> devices = deviceRepository.findAllByOwner(device.getOwner());
        assertTrue(devices.size() >= 1);
    }

    @AfterAll
    public void testDelete() {
        deviceRepository.deleteAll();
        assertEquals(deviceRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}