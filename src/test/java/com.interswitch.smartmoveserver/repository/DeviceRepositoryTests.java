package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.repository.DeviceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceRepositoryTests {
    @Autowired
    DeviceRepository deviceRepository;


    @Test
    public void testSaveAndFindById() {
        Device device = new Device();
        device.setDeviceId("012345");
        device.setOwnerId("123456789");
        device.setType(0);
        device.setStatus("");
        device.setVersionHardware("1.0.0");
        device.setVersionHardware("2.0.2");
        device.setVersionEmv("3.0.3");
        device.setPeriodGps("15");
        device.setPeriodTransactionUpload("180");
        device.setFareType(0);

        Device savedDevice = deviceRepository.save(device);
        deviceRepository.findById(savedDevice.getDeviceId()).ifPresent(device1 -> {
            assertThat(device1.getOwnerId()).isEqualTo(device.getOwnerId());
            assertThat(device1.getType()).isEqualTo(device.getType());
            assertThat(device1.getStatus()).isEqualTo(device.getStatus());
            assertThat(device1.getVersionHardware()).isEqualTo(device.getVersionHardware());
            assertThat(device1.getVersionSoftware()).isEqualTo(device.getVersionSoftware());
            assertThat(device1.getVersionEmv()).isEqualTo(device.getVersionEmv());
            assertThat(device1.getPeriodGps()).isEqualTo(device.getPeriodGps());
            assertThat(device1.getPeriodTransactionUpload()).isEqualTo(device.getPeriodTransactionUpload());
            assertThat(device1.getFareType()).isEqualTo(device.getFareType());

        });
    }

    @Test
    public void testFindAll() {
        Device device = new Device();
        device.setDeviceId("012345");
        device.setOwnerId("123456789");
        device.setType(0);
        device.setStatus("");
        device.setVersionHardware("1.0.0");
        device.setVersionHardware("2.0.2");
        device.setVersionEmv("3.0.3");
        device.setPeriodGps("15");
        device.setPeriodTransactionUpload("180");
        device.setFareType(0);
        Device device1 = new Device();
        device1.setDeviceId("012346");
        device1.setOwnerId("123456789");
        device1.setType(0);
        device1.setStatus("");
        device1.setVersionHardware("1.0.0");
        device1.setVersionHardware("2.0.2");
        device1.setVersionEmv("3.0.3");
        device1.setPeriodGps("15");
        device1.setPeriodTransactionUpload("180");
        device1.setFareType(2);
        deviceRepository.save(device);
        deviceRepository.save(device1);
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
    public void testDelete() {
        Device device = new Device();
        device.setDeviceId("012345");
        device.setOwnerId("123456789");
        device.setType(0);
        device.setStatus("");
        device.setVersionHardware("1.0.0");
        device.setVersionHardware("2.0.2");
        device.setVersionEmv("3.0.3");
        device.setPeriodGps("15");
        device.setPeriodTransactionUpload("180");
        device.setFareType(1);
        device.setVersionEmv("hello");
        Device savedDevice = deviceRepository.save(device);
        deviceRepository.deleteById(savedDevice.getDeviceId());
        assertThat(deviceRepository.findById(savedDevice.getDeviceId())).isEmpty();
    }
}