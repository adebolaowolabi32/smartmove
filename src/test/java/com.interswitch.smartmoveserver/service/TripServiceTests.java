package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.*;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TripServiceTests {

    @Autowired
    private TripService tripService;

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
        //User testUser = buildTestUser();
        User testUser = new User();
        testUser.setFirstName("Alice");
        testUser.setLastName("Com");
        testUser.setRole(com.interswitch.smartmoveserver.model.Enum.Role.VEHICLE_OWNER);
        testUser.setEmail(new RandomUtil().nextString().concat("@example.com"));
        testUser.setMobileNo(RandomUtil.getRandomNumber(11));
        testUser.setUsername(testUser.getEmail());
        testUser.setPassword(""+new RandomUtil().nextString());

        User owner = userRepository.save(testUser);
        vehicle = new Vehicle();
        vehicle.setName("vehicle_A");
        vehicle.setDevice(new Device());
        vehicle.setRegNo("LAG0000002");
        vehicle.setOwner(owner);
        vehicle.setEnabled(true);

        trip = new Trip();
        trip.setReferenceNo(new RandomUtil(5).nextString());
        trip.setDriver(userRepository.save(buildTestUser(com.interswitch.smartmoveserver.model.Enum.Role.DRIVER)));
        trip.setFare(5000);
        vehicle = vehicleRepository.save(vehicle);
        trip.setVehicle(vehicle);

        schedule = new Schedule();
        VehicleCategory vehicleCategory = new VehicleCategory();
        vehicleCategory.setOwner(owner);
        vehicleCategory.setCapacity(50);

        VehicleMake vehicleMake = vehicleMakeRepository.save(new VehicleMake(0, "Macopolo"));

        vehicleCategory.setMake(vehicleMake);
        vehicleCategory.setMode(com.interswitch.smartmoveserver.model.Enum.TransportMode.BUS);
        vehicleCategory.setNoColumns(10);
        vehicleCategory.setNoRows(5);
        vehicleCategory.setName("Maco 123");
        vehicleCategory.setColor("Red");
        vehicleCategory.setModel(new VehicleModel(0, vehicleMake, "2020 Model", "2020"));
        vehicleCategory = vehicleCategoryRepository.save(vehicleCategory);

        schedule.setVehicle(vehicleCategory);
        schedule.setArrivalDate(LocalDate.now());
        schedule.setDepartureDate(LocalDate.now());
        schedule.setDepartureTime(LocalTime.now());
        schedule.setArrivalTime(schedule.getDepartureTime().plusHours(5));
        schedule.setRoute(new Route());
        trip.setSchedule(scheduleRepository.save(schedule));

    }

    @After
    public void tearDown() {
        //delete all composite entities
        tripRepository.deleteAll();
        scheduleRepository.deleteAll();
        vehicleCategoryRepository.deleteAll();
        vehicleMakeRepository.deleteAll();
        vehicleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testFindAll(){
        savedTrip = tripService.save(trip);
        List<Trip> trips = tripService.findAll();
        assertTrue(trips.size()>=1);
    }

    @Test
    public void testSave(){
        savedTrip = tripService.save(trip);
        assertTrue(savedTrip.getId() > 0);
    }

    @Test
    public void testFindById(){
        savedTrip = tripService.save(trip);
        Trip trip = tripService.findById(savedTrip.getId());
        assertTrue(trip.getId()==savedTrip.getId());
    }

    @Test
    public void testUpdate(){
        savedTrip = tripService.save(trip);
        savedTrip.setFare(100);
        Trip updatedTrip  = tripService.update(savedTrip);
        assertTrue(updatedTrip.getFare()==savedTrip.getFare());
    }

    /*@Test
    public void testDelete(){
        savedTrip = tripService.save(trip);
        tripService.delete(savedTrip.getId());
        assertTrue(tripService.findById(savedTrip.getId())==null) ;
    }
*/
}
