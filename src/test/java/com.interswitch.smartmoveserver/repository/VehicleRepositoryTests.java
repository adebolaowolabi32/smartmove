package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class VehicleRepositoryTests {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    private Vehicle vehicle;
    private Vehicle savedVehicle;

    @BeforeAll
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
            assertEquals(vehicle1.getDevice(), vehicle1.getDevice());
            assertEquals(vehicle1.getOwner(), vehicle1.getOwner());
            assertEquals(vehicle1.getRegNo(), vehicle1.getRegNo());
        });
    }

   /* @Test
    public void testFindAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles.size()).isGreaterThanOrEqualTo(2);
    }*/

/*
    @Test
    public void testFindByOwner() {
        List<Vehicle> vehicles = vehicleRepository.findAllByOwner(savedVehicle.getOwner());
        assertThat(vehicles.size()).isGreaterThanOrEqualTo(1);
    }
*/


    @AfterAll
    public void testDelete() {
        vehicleRepository.deleteAll();
        assertEquals(vehicleRepository.findAll().iterator().hasNext(), false);
        userRepository.deleteAll();
    }
}

