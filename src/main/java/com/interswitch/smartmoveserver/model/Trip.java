package com.interswitch.smartmoveserver.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 7/17/2020
 */
@Data
@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String referenceNo;

    @ManyToOne
    @JoinColumn(name = "driver")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "route")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "vehicle")
    private Vehicle vehicle;

    private String departure;

    private String arrival;

    private LocalDateTime departureDateTime;

    private LocalDateTime arrivalDateTime;
}
