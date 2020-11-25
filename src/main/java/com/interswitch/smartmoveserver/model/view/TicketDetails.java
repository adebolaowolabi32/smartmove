package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/*
 * Created by adebola.owolabi on 7/29/2020
 */
@Data
public class TicketDetails implements Serializable {

    private User operator;

    private String contactMobile;

    private String contactEmail;

    private String address;

    private String nextOfKinName;

    private String nextOfKinMobile;

    private Enum.PaymentMode paymentMode;

    private String bvn;

    private String tripId;

    private Schedule schedule;

    private Schedule returnSchedule;

    private String scheduleId;

    private Trip trip;

    private String startTerminalName;

    private String stopTerminalName;

    private int noOfPassengers;

    private List<String> seats;

    private double totalFare;

    private LocalDate departure;

    private List<Passenger> passengers;

    private List<Ticket> tickets;

    private String referenceNo;

    private String paymentReferenceNo;

    private String bookingDate;

    private LocalDate returnDate;

    private List<String> countries;

    private List<FeeConfiguration> fees;

    private List<FeeDetails> appliedFees;
}
