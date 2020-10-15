package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 7/17/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Enum.TransportMode mode;

    @ManyToOne
    @JoinColumn(name = "driver")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "schedule")
    private Schedule schedule;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
}
