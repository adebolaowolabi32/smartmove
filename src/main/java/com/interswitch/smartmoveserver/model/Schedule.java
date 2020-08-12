package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departure;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrival;

    private String departureString;

    private String arrivalString;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private boolean enabled;

}
