package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.model.Ticket;
import com.interswitch.smartmoveserver.model.Trip;
import lombok.Data;
import org.springframework.util.AutoPopulatingList;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/*
 * Created by adebola.owolabi on 7/29/2020
 */
@Data
public class TicketDetails implements Serializable {

    private String contactMobile;

    private String contactEmail;

    private String nextOfKinName;

    private String nextOfKinMobile;

    private Enum.PaymentMode paymentMode;

    private String bvn;

    private String name;

    private String gender;

    private String nationality;

    private Enum.IdCategory idCategory;

    private String idNumber;

    @Enumerated(EnumType.STRING)
    private Enum.SeatClass seatClass;

    private String seatNo;

    private String tripId;

    private Schedule schedule;

    private String scheduleId;

    private Trip trip;

    private String startTerminalName;

    private String stopTerminalName;

    private int noOfPassengers;

    private List<String> seats;

    private double totalFare;

    private LocalDate departure;

    private Passenger passenger;

    private AutoPopulatingList<Passenger> passengers;

    private List<Ticket> tickets;

    private List<Schedule> schedules;

    private String referenceNo;

    private String paymentReferenceNo;

    private String bookingDate;

    private LocalDate returnDate;

    private List<String> countries;
}
