package com.interswitch.smartmoveserver.config;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Manifest;
import com.interswitch.smartmoveserver.model.Seat;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.SeatRepository;
import com.interswitch.smartmoveserver.service.ManifestService;
import com.interswitch.smartmoveserver.service.TripService;
import com.interswitch.smartmoveserver.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author adebola.owolabi
 */
@Component
public class ApplicationStartup implements CommandLineRunner {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    UserService userService;

    @Autowired
    ManifestService manifestService;

    @Autowired
    TripService tripService;

    @Autowired
    SeatRepository seatRepo;


    @Override
    public void run(String... args) {
       User adminUser = new User();
        adminUser.setFirstName("Smart");
        adminUser.setLastName("Move");
        adminUser.setUsername("smart.move13@interswitch.com");
        adminUser.setPassword("Password123$");
        adminUser.setEmail("smart.move13@interswitch.com");
        adminUser.setAddress("Lagos Nigeria");
        adminUser.setRole(Enum.Role.ISW_ADMIN);
        adminUser.setEnabled(true);
        userService.setUp(adminUser);
        logger.info("System Administrator created successfully!");

        User driver = new User();
        adminUser.setFirstName("Suleiman");
        adminUser.setLastName("Adelabu");
        adminUser.setUsername("sule.adelabu");
        adminUser.setPassword("Password123$");
        adminUser.setEmail("earnest.suru@gmail.com");
        adminUser.setAddress("Lagos Nigeria");
        adminUser.setRole(Enum.Role.DRIVER);
        adminUser.setEnabled(true);
        userService.setUp(adminUser);
        logger.info("Driver created successfully!");
        //loadManifestData(7);

    }

    public void loadManifestData(long tripId){

        manifestService.deleteAll();
        seatRepo.deleteAll();

        Manifest manifest = new Manifest();
        manifest.setBoarded(true);
        manifest.setContactEmail("eb-things@gmail.com");
        manifest.setContactMobile("09029898722");
        manifest.setIdNumber("NG-2989876510");
        manifest.setGender("Male");
        manifest.setName("Joel Jacintha");
        manifest.setNationality("Nigeria");
        manifest.setNextOfKinMobile("08038972655");
        manifest.setNextOfKinName("Joel Anyanwu");
        manifest.setTrip(tripService.findById(tripId));
        Seat seat = new Seat();
        seat.setSeatId("01");
        seat.setRowNo(2);
        seat.setColumnNo(1);
        seatRepo.save(seat);
        manifest.setSeat(seat);
        manifest.setAddress("58 Jakande,Lagos");
        manifestService.save(manifest);
        //
        manifest = new Manifest();
        manifest.setBoarded(true);
        manifest.setContactEmail("ay@gmail.com");
        manifest.setContactMobile("08029898722");
        manifest.setIdNumber("NG-2989876500");
        manifest.setGender("Female");
        manifest.setName("Ken Wale");
        manifest.setNationality("Nigeria");
        manifest.setNextOfKinMobile("08030972655");
        manifest.setNextOfKinName("Ayomide Wale");
        manifest.setTrip(tripService.findById(tripId));
        seat = new Seat();
        seat.setSeatId("02");
        seat.setRowNo(2);
        seat.setColumnNo(2);
        seatRepo.save(seat);
        manifest.setSeat(seat);
        manifest.setAddress("22 Captain Black Road,Lagos.");
        manifestService.save(manifest);
    }
}