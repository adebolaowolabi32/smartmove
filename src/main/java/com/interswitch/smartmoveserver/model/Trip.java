package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 7/17/2020
 */
@Data
@Entity
@Table(name = "trips")
public class Trip implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String referenceNo;

    private double fare;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode transportMode;

    @ManyToOne
    @JoinColumn(name = "driver")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "schedule")
    private Schedule schedule;

    @ManyToOne
    private VehicleCategory vehicle;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departure;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrival;

    private String departureString;

    private String arrivalString;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
}
