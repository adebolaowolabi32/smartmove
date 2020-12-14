package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.*;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
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
public class ManifestServiceTests {

    private Trip trip;
    private Schedule schedule;
    private Schedule savedSchedule;
    private Manifest manifest;
    private Manifest savedManifest;
    private Seat seat;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private VehicleCategoryRepository vehicleCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleMakeRepository vehicleMakeRepository;

    @Autowired
    private ManifestService manifestService;

    @Before
    public void setUp() {
        User testUser = new User();
        testUser.setFirstName("Alice");
        testUser.setLastName("Com");
        testUser.setRole(com.interswitch.smartmoveserver.model.Enum.Role.OPERATOR);
        testUser.setEmail(new RandomUtil().nextString().concat("@example.com"));
        testUser.setMobileNo(RandomUtil.getRandomNumber(11));
        testUser.setUsername(testUser.getEmail());
        testUser.setPassword(""+new RandomUtil().nextString());

        User owner = userRepository.save(testUser);

        manifest = new Manifest();
        manifest.setAddress("Lagos");
        manifest.setIdNumber("1022");
        manifest.setNextOfKinName("taiwo");
        manifest.setNextOfKinMobile("09021711733");
        manifest.setNationality("Nigeria");
        manifest.setName("Abraham");
        manifest.setGender("Male");
        manifest.setContactMobile("09021811833");
        manifest.setContactEmail("abraham@rocketmail.com");
        manifest.setBvn("1239098788");
        manifest.setSeatNo("12");
        //composite entities
        seat = new Seat();
        seat.setColumnNo(1);
        seat.setRowNo(2);
        seat.setSeatClass(com.interswitch.smartmoveserver.model.Enum.SeatClass.STANDARD);

        Vehicle vehicle = new Vehicle();
        vehicle.setRegNo("ABC120908UL");
        vehicle.setName("Marcopolo 505");

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

        vehicle.setCategory(vehicleCategory);
        vehicle = vehicleRepository.save(vehicle);

        seat = seatRepository.save(seat);
        manifest.setSeat(seat);

        schedule = new Schedule();
        schedule.setVehicle(vehicleCategory);
        schedule.setArrivalDate(LocalDate.now());
        schedule.setDepartureDate(LocalDate.now());
        schedule.setDepartureTime(LocalTime.now());
        schedule.setArrivalTime(schedule.getDepartureTime().plusHours(5));


        schedule.setStartTerminal(Terminal.builder()
                .country("Nigeria").state("Lagos").code("LAG")
                .lga("Alimosho").owner(buildTestUser())
                .name("LAGOS TERMINAL 1").mode(com.interswitch.smartmoveserver.model.Enum.TransportMode.BUS)
                .build());

        schedule.setStopTerminal(Terminal.builder()
                .country("Nigeria").state("Lagos").code("LAG")
                .lga("Agege").owner(buildTestUser())
                .name("LAGOS TERMINAL 2").mode(com.interswitch.smartmoveserver.model.Enum.TransportMode.BUS)
                .build());

        savedSchedule = scheduleRepository.save(schedule);
        manifest.setSchedule(savedSchedule);
        manifest.setIdCategory(Enum.IdCategory.NATIONAL_ID);

    }

    @After
    public void tearDown() {
        manifestService.deleteAll();
        scheduleRepository.deleteAll();
        seatRepository.deleteAll();
        vehicleRepository.deleteAll();
        vehicleMakeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testFindAll() {
        savedManifest = manifestService.save(manifest);
        List<Manifest> manifests = manifestService.findAll();
        assertTrue(manifests.size()>=1);
    }

    /*@Test
    public void testFindAllPaginated() {
        savedManifest = manifestService.save(manifest);
        Page<Manifest> manifestPages = manifestService.findAllPaginated(1,5);
        assertTrue(manifestPages.getTotalPages()>=1);
    }

    @Test
    public void testFindPaginatedManifestByScheduleId() {
        savedManifest = manifestService.save(manifest);
        Page<Manifest> manifestPages = manifestService.findPaginatedManifestByScheduleId(1,5,savedSchedule.getId());
        assertTrue(manifestPages.getTotalPages()>=1);
    }

    @Test
    public void testSave() {
        savedManifest = manifestService.save(manifest);
        assertTrue(savedManifest.getId() > 0);
    }

    @Test
    public void testFindById() {
        savedManifest = manifestService.save(manifest);
        assertTrue(manifestService.findById(savedManifest.getId())!=null);

    }

    @Test
    public void testUpdate() {
        savedManifest = manifestService.save(manifest);
        savedManifest.setSeatNo("13");
        Manifest updatedManifest = manifestService.update(savedManifest);
        assertTrue(updatedManifest.getSeatNo()=="13");
    }*/

}
