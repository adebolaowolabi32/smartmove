package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.Schedule;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/*
 * Created by adebola.owolabi on 8/4/2020
 */
@Data
public class ScheduleBooking implements Serializable {

    private String scheduleId;

    private Route schedule;

    private String startTerminal;

    private String stopTerminal;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departure;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    private int noOfPassengers;

    private List<Schedule> schedules;

    private List<Schedule> returnSchedules;

    private boolean roundTrip;

    private boolean invalid;
}
