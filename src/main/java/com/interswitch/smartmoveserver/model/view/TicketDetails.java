package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.Ticket;
import com.interswitch.smartmoveserver.model.Trip;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    //
    private Enum.PaymentMode paymentMode;

    private String tripId;

    private Trip trip;

    private String routeId;

    private Route route;

    private int noOfPassengers;

    private List<String> seats;

    private double totalFare;

    private String name;

    private String gender;

    private String nationality;

    //
    private Enum.IdCategory idCategory;

    private String idNumber;

    @Enumerated(EnumType.STRING)
    private Enum.SeatClass seatClass;

    private String seatNo;

    private LocalDateTime tripDate;

    private List<Ticket> tickets;

    @Column(unique = true)
    private String referenceNo;

    private String paymentReferenceNo;

    private LocalDateTime bookingDate;

    private List<String> countries;
}
