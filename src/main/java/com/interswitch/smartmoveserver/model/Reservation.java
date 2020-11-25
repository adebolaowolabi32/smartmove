package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 7/18/2020
 */
@Data
@Entity
@Table(name = "reservations")
@EntityListeners(AuditingEntityListener.class)
public class Reservation extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String passengerName;

    @ManyToOne
    @JoinColumn(name = "trip")
    private Trip trip;

    @Enumerated(EnumType.STRING)
    private Enum.SeatClass seatClass;

    @ManyToOne
    @JoinColumn(name = "seat")
    private Seat seat;
}
