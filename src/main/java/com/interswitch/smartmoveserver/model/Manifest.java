package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 7/18/2020
 */
@Data
@Entity
@Table(name = "manifests")
public class Manifest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "seat")
    private Seat seat;

    private String name;

    private String gender;

    private String nationality;

    @Enumerated(EnumType.STRING)
    private Enum.IdCategory idCategory;

    private String idNumber;

    private String contactMobile;

    private String contactEmail;

    private String nextOfKinName;

    private String nextOfKinMobile;

    @ManyToOne
    @JoinColumn(name = "trip")
    private Trip trip;

    private boolean boarded;

    private boolean completed;

    private LocalDateTime timeofBoarding;

    private LocalDateTime timeofCompletion;

}
