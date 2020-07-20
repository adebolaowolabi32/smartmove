package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 7/18/2020
 */
@Data
@Entity
@Table(name = "manifests")
public class Manifest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "seat")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip")
    private Trip trip;

    private boolean boarded;

    private boolean completed;

    private LocalDateTime timeofBoarding;

    private LocalDateTime timeofCompletion;

}
