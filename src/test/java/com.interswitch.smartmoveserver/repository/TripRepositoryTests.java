package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TripRepositoryTests {

    @Autowired
    private TripRepository tripRepository;

    private Trip savedTrip;

    private Trip trip;

    private Vehicle vehicle;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMakeRepository vehicleMakeRepository;

    @Autowired
    private VehicleCategoryRepository vehicleCategoryRepository;

    private Schedule schedule;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Before
    public void setUp() {

        User owner = userRepository.save(buildTestUser());
        vehicle = new Vehicle();
        vehicle.setName("vehicle_A");
        vehicle.setDevice(new Device());
        vehicle.setRegNo("LAG0000002");
        vehicle.setOwner(owner);
        vehicle.setEnabled(true);

        trip = new Trip();
        trip.setReferenceNo(new RandomUtil(5).nextString());
        trip.setDriver(userRepository.save(buildTestUser(Enum.Role.DRIVER)));
        trip.setFare(5000);
        trip.setMode(Enum.TransportMode.BUS);
        vehicle = vehicleRepository.save(vehicle);
        trip.setVehicle(vehicle);

        schedule = new Schedule();
        VehicleCategory vehicleCategory = new VehicleCategory();
        vehicleCategory.setOwner(owner);
        vehicleCategory.setCapacity(50);

        VehicleMake vehicleMake = vehicleMakeRepository.save(new VehicleMake(0, "Macopolo"));

        vehicleCategory.setMake(vehicleMake);
        vehicleCategory.setMode(Enum.TransportMode.BUS);
        vehicleCategory.setNoColumns(10);
        vehicleCategory.setNoRows(5);
        vehicleCategory.setName("Maco 123");
        vehicleCategory.setColor("Red");
        vehicleCategory.setModel(new VehicleModel(0, vehicleMake, "2020 Model", "2020"));
        vehicleCategory = vehicleCategoryRepository.save(vehicleCategory);

        schedule.setVehicle(vehicleCategory);
        schedule.setArrival(LocalDateTime.of(2020, Month.AUGUST, 26, 8, 30));
        schedule.setDeparture(LocalDateTime.of(2020, Month.AUGUST, 26, 10, 30));
        schedule.setArrivalString(schedule.getArrival().toString());
        schedule.setDepartureString(schedule.getDeparture().toString());


        schedule.setStartTerminal(Terminal.builder()
                .country("Nigera").state("Lagos").code("LAG")
                .lga("Alimosho").location("").owner(buildTestUser())
                .name("LAGOS TERMINAL 1").mode(Enum.TransportMode.BUS)
                .build());

        schedule.setStopTerminal(Terminal.builder()
                .country("Nigera").state("Lagos").code("LAG")
                .lga("Agege").location("").owner(buildTestUser())
                .name("LAGOS TERMINAL 2").mode(Enum.TransportMode.BUS)
                .build());

        trip.setSchedule(scheduleRepository.save(schedule));
        savedTrip = tripRepository.save(trip);
        assertTrue(savedTrip.getId() > 0);
    }

    @After
    public void tearDown() {
        //delete all composite entities
        userRepository.deleteAll();
        vehicleRepository.deleteAll();
        vehicleMakeRepository.deleteAll();
        vehicleCategoryRepository.deleteAll();
        scheduleRepository.deleteAll();
        tripRepository.deleteAll();
    }
    @Test
    public void testFindById() {
        tripRepository.findById(savedTrip.getId())
                .ifPresent(trp -> {
                    assertThat(trp.getDriver()).isEqualTo(trip.getDriver());
                    assertThat(trp.getFare()).isEqualTo(trip.getFare());
                    assertThat(trp.getOwner()).isEqualTo(trip.getOwner());
                    assertThat(trp.getMode()).isEqualTo(trip.getMode());
                    assertThat(trp.getReferenceNo()).isEqualTo(trip.getReferenceNo());
                    assertThat(trp.getVehicle()).isEqualTo(trip.getVehicle());
                    assertThat(trp.getSchedule()).isEqualTo(trip.getSchedule());

                });
    }
    @Test
    public void testFindByReferenceNo() {
        Trip tripFrmDb = tripRepository.findByReferenceNo(savedTrip.getReferenceNo());
        assertThat(tripFrmDb!=null);
        assertThat(tripFrmDb.getDriver()).isEqualTo(trip.getDriver());
        assertThat(tripFrmDb.getFare()).isEqualTo(trip.getFare());
        assertThat(tripFrmDb.getOwner()).isEqualTo(trip.getOwner());
        assertThat(tripFrmDb.getMode()).isEqualTo(trip.getMode());
        assertThat(tripFrmDb.getReferenceNo()).isEqualTo(trip.getReferenceNo());
        assertThat(tripFrmDb.getVehicle()).isEqualTo(trip.getVehicle());
        assertThat(tripFrmDb.getSchedule()).isEqualTo(trip.getSchedule());
    }
    @Test
    public void testFindByDriverUsername() {
        List<Trip> trips = tripRepository.findByDriverUsername(savedTrip.getDriver().getUsername());
        assertTrue(trips.size()>0);
    }
    @Test
    public void testfindAll() {
        List<Trip> trips = tripRepository.findAll();
        assertTrue(trips.size()>0);
    }
    @Test
    public void testDelete() {
        tripRepository.deleteById(savedTrip.getId());
        assertTrue(tripRepository.findById(savedTrip.getId()).isPresent()==false) ;
    }

}
