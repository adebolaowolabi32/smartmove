package com.interswitch.smartmoveserver.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ScheduleSearchResult {

    private String startTerminal;

    private String stopTerminal;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departure;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    private List<ScheduleView> schedules;

    private List<ScheduleView> returnSchedules;

    @JsonIgnore
    private boolean roundTrip;


    @Data
   public static class ScheduleView{

        private String vehicleName;
        private long vehicleId;
        private String vehiclePictureUrl;
        private String vehicleMode;
        private long vehicleCapacity;
        private long availableSeats;
        private boolean acAvailable;
        private long scheduleId;
        private double fare;

        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        private LocalTime departureTime;

        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        private LocalTime arrivalTime;

        private long duration;
    }

}
