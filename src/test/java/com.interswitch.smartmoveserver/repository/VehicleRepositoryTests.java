package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleRepositoryTests {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    private Vehicle vehicle;
    private Vehicle savedVehicle;

    @Before
    public void setUp() {
        vehicle = new Vehicle();
        vehicle.setName("vehicle_A");
        vehicle.setDevice(new Device());
        User user = buildTestUser();
        userRepository.save(user);
        vehicle.setOwner(user);
        vehicle.setRegNo("LAG0000002");
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setName("vehicle_B");
        vehicle1.setDevice(new Device());
        vehicle1.setOwner(user);
        vehicle1.setRegNo("LAG0000003");
        assertNotNull(vehicleRepository.save(vehicle1));
        savedVehicle = vehicleRepository.save(vehicle);
        assertNotNull(savedVehicle);
    }

    @Test
    public void testFindById() {
        vehicleRepository.findById(savedVehicle.getId()).ifPresent(vehicle1 -> {
            assertThat(vehicle1.getDevice()).isEqualTo(vehicle1.getDevice());
            assertThat(vehicle1.getOwner()).isEqualTo(vehicle1.getOwner());
            assertThat(vehicle1.getRegNo()).isEqualTo(vehicle1.getRegNo());
        });
    }

    @Test
    public void testFindAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByOwner() {
        List<Vehicle> vehicles = vehicleRepository.findAllByOwner(savedVehicle.getOwner());
        assertThat(vehicles.size()).isGreaterThanOrEqualTo(1);
    }


    @After
    public void testDelete() {
        vehicleRepository.deleteAll();
        assertEquals(vehicleRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}

