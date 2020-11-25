package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Transport mode is required.")
    private Enum.TransportMode mode;

    @ManyToOne
    @JoinColumn(name = "startTerminal")
    @NotNull(message = "Start terminal is required.")
    private Terminal startTerminal;

    @ManyToOne
    @JoinColumn(name = "stopTerminal")
    @NotNull(message = "Stop terminal is required.")
    private Terminal stopTerminal;

    @ManyToOne
    @NotNull(message = "Vehicle category is required.")
    private VehicleCategory vehicle;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Departure date is required.")
    @FutureOrPresent
    private LocalDate departureDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "Departure time is required.")
    @FutureOrPresent
    private LocalTime departureTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Arrival date is required.")
    @Future
    private LocalDate arrivalDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "Arrival time is required.")
    @Future
    private LocalTime arrivalTime;

    private String duration;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private boolean enabled;

}