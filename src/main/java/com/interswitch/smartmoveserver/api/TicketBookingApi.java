package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Seat;
import com.interswitch.smartmoveserver.model.request.ScheduleSearchRequest;
import com.interswitch.smartmoveserver.model.response.ScheduleSearchResult;
import com.interswitch.smartmoveserver.service.SeatService;
import com.interswitch.smartmoveserver.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/booking")
public class TicketBookingApi {

    @Autowired
    TicketService ticketService;

    @Autowired
    SeatService seatService;


    @PostMapping(value = "/search", produces = "application/json")
    private ScheduleSearchResult findByOwnerId(@Validated @RequestBody ScheduleSearchRequest scheduleSearch) {
        return ticketService.findBookingFromApi(scheduleSearch);
    }

    @GetMapping(value = "/seats", produces = "application/json")
    private Set<Seat> findSeatsByVehicleId(@Validated @RequestParam("vehicleId") long  vehicleId) {
        return seatService.findSeatsByVehicleId(vehicleId);
    }
}
