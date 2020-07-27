package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

/*
 * Created by adebola.owolabi on 7/18/2020
 */
@Data
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String seatId;

    @ManyToOne
    @JoinColumn(name = "vehicle")
    private Vehicle vehicle;

    private int rowNo;

    private int columnNo;

    @Enumerated(EnumType.STRING)
    private Enum.SeatClass seatClass;
}
