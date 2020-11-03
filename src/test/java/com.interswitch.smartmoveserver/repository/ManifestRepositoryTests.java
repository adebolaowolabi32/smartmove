package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.interswitch.smartmoveserver.util.TestUtils.buildTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class ManifestRepositoryTests {

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
    private ManifestRepository manifestRepository;

    @BeforeAll
    public void setUp() {

        User testUser = new User();
        testUser.setFirstName("Alice");
        testUser.setLastName("Com");
        testUser.setRole(Enum.Role.OPERATOR);
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
        seat.setSeatClass(Enum.SeatClass.STANDARD);

        Vehicle vehicle = new Vehicle();
        vehicle.setRegNo("ABC120908UL");
        vehicle.setName("Marcopolo 505");

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

        vehicle.setCategory(vehicleCategory);
        vehicle = vehicleRepository.save(vehicle);

        seat.setVehicle(vehicle);
        seat = seatRepository.save(seat);
        manifest.setSeat(seat);

        schedule = new Schedule();
        schedule.setVehicle(vehicleCategory);
        schedule.setArrivalDate(LocalDate.now());
        schedule.setDepartureDate(LocalDate.now());
        schedule.setDepartureTime(LocalTime.now());
        schedule.setArrivalTime(schedule.getDepartureTime().plusHours(5));

        schedule.setStartTerminal(Terminal.builder()
                .country("Nigera").state("Lagos").code("LAG")
                .lga("Alimosho").owner(buildTestUser())
                .name("LAGOS TERMINAL 1").mode(Enum.TransportMode.BUS)
                .build());

        schedule.setStopTerminal(Terminal.builder()
                .country("Nigera").state("Lagos").code("LAG")
                .lga("Agege").owner(buildTestUser())
                .name("LAGOS TERMINAL 2").mode(Enum.TransportMode.BUS)
                .build());

        savedSchedule = scheduleRepository.save(schedule);
        manifest.setSchedule(savedSchedule);
        manifest.setIdCategory(Enum.IdCategory.NATIONAL_ID);
        savedManifest = manifestRepository.save(manifest);
        //manifest.setTrip();
        assertTrue(savedManifest.getId() > 0);
    }

    @AfterAll
    public void tearDown() {

     manifestRepository.deleteAll();
     scheduleRepository.deleteAll();
     seatRepository.deleteAll();
     vehicleRepository.deleteAll();
     vehicleMakeRepository.deleteAll();
     userRepository.deleteAll();

    }

    @Test
    public void testFindByTripId(){
        manifestRepository.findById(savedManifest.getId())
                .ifPresent(mnf -> {
                    assertThat(mnf.getAddress()).isEqualTo(manifest.getAddress());
                    assertThat(mnf.getBvn()).isEqualTo(manifest.getBvn());
                    assertThat(mnf.getName()).isEqualTo(manifest.getName());
                    assertThat(mnf.getSeatNo()).isEqualTo(manifest.getSeatNo());
                    assertThat(mnf.getContactEmail()).isEqualTo(manifest.getContactEmail());
                    assertThat(mnf.getContactMobile()).isEqualTo(manifest.getContactMobile());
                });
    }

    @Test
    public void testFindAll(){
        List<Manifest> manifests = manifestRepository.findAll();
        assertTrue(manifests.size()>0);
    }

    @Test
    public void testFindByScheduleId(){
        Pageable pageable = PageRequest.of(1, 5);
        Page<Manifest> manifestPage = manifestRepository.findByScheduleId(pageable,savedSchedule.getId());
        assertTrue(manifestPage.getTotalElements() >=1);
    }
}
