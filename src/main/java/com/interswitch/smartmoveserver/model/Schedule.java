package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/*
 * Created by adebola.owolabi on 8/7/2020
 */
@Data
@Entity
@Table(name = "schedules")
public class Schedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long fare;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode mode;

    @ManyToOne
    @JoinColumn(name = "startTerminal")
    private Terminal startTerminal;

    @ManyToOne
    @JoinColumn(name = "stopTerminal")
    private Terminal stopTerminal;

    @ManyToOne
    private VehicleCategory vehicle;

    @DateTimeFormat(pattern = "MMMM dd, yyyy")
    private LocalDate departureDate;

    @DateTimeFormat(pattern = "HH:mm aa")
    private LocalTime departureTime;

    @DateTimeFormat(pattern = "MMMM dd, yyyy")
    private LocalDate arrivalDate;

    @DateTimeFormat(pattern = "HH:mm aa")
    private LocalTime arrivalTime;

    private String duration;

    private String departureDateString;

    private String arrivalDateString;

    private String departureTimeString;

    private String arrivalTimeString;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private boolean enabled;

}
