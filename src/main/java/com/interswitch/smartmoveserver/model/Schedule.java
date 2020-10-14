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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime departureTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate arrivalDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime arrivalTime;

    private String duration;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private boolean enabled;

}
