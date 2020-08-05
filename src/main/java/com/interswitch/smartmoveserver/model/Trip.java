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

    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "driver")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "route")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "vehicle")
    private Vehicle vehicle;

    @DateTimeFormat(pattern = "MMM dd yyyy HH:mm aa")
    private LocalDateTime departureObj;

    @DateTimeFormat(pattern = "MMM dd yyyy HH:mm aa")
    private LocalDateTime arrivalObj;

    private String departure;

    private String arrival;
}
