package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.request.ScheduleSearchRequest;
import com.interswitch.smartmoveserver.model.view.ScheduleBooking;
import com.interswitch.smartmoveserver.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class TicketBookingApi {

    @Autowired
    TicketService ticketService;

    @PostMapping(value = "/search", produces = "application/json")
    private ScheduleBooking findByOwnerId(@Validated @RequestBody  ScheduleSearchRequest scheduleSearch) {
        return ticketService.findBookingFromApi(scheduleSearch);
    }
}
