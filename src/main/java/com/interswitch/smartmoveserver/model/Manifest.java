package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "manifests")
public class Manifest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "seat")
    @JsonIgnore
    private Seat seat;

    private String seatNo;

    private String name;

    private String address;

    private String gender;

    private String bvn;

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

    @ManyToOne
    @JoinColumn(name = "schedule")
    private Schedule schedule;

    private boolean boarded;

    private boolean completed;

    private LocalDateTime timeofBoarding;

    private LocalDateTime timeofCompletion;
}
