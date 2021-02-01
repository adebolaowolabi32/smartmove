package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.request.ScheduleSearchRequest;
import com.interswitch.smartmoveserver.model.response.ScheduleSearchResult;
import com.interswitch.smartmoveserver.model.view.ScheduleBooking;
import com.interswitch.smartmoveserver.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking")
public class TicketBookingApi {

    @Autowired
    TicketService ticketService;

    @PostMapping(value = "/search", produces = "application/json")
    private ScheduleSearchResult findByOwnerId(@Validated @RequestBody ScheduleSearchRequest scheduleSearch) {
        return ticketService.findBookingFromApi(scheduleSearch);
    }
}
