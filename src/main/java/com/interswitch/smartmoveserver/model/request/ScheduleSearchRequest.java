package com.interswitch.smartmoveserver.model.request;

import com.interswitch.smartmoveserver.model.view.ScheduleBooking;
import com.interswitch.smartmoveserver.util.DateUtil;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class ScheduleSearchRequest {

    @Min(value=1, message = "agent owner Id is required" )
    private long ownerId;

    @NotEmpty(message = "departure point required")
    private String startTerminal;

    @NotEmpty(message = "destination point required ")
    private String stopTerminal;

    @NotEmpty(message = "departure date required")
    private String departureDate;

    private String returnDate;

    private int noOfPassengers;

    private boolean roundTrip;

    public ScheduleBooking mapToScheduleBooking() {
        ScheduleBooking scheduleBooking = new ScheduleBooking();
        scheduleBooking.setReturnDate(this.getReturnDate().isEmpty() ? null : DateUtil.textToLocalDate(this.getReturnDate()));
        scheduleBooking.setDeparture(DateUtil.textToLocalDate(this.getDepartureDate()));
        scheduleBooking.setNoOfPassengers(this.getNoOfPassengers());
        scheduleBooking.setStartTerminal(this.getStartTerminal());
        scheduleBooking.setStopTerminal(this.getStopTerminal());
        return scheduleBooking;
    }

}
