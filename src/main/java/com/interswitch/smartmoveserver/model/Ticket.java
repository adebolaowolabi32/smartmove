package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 7/25/2020
 */
@Data
@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String referenceNo;

    private String paymentReferenceNo;

    private String passengerName;

    @ManyToOne
    @JoinColumn(name = "operator")
    private User operator;

    @ManyToOne
    @JoinColumn(name = "trip")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "schedule")
    private Schedule schedule;

    private double fare;

    @Enumerated(EnumType.STRING)
    private Enum.SeatClass seatClass;

    @ManyToOne
    @JoinColumn(name = "seat")
    private Seat seat;

    private String seatNo;

    private String bookingDate;

    private boolean refunded = false;
}
