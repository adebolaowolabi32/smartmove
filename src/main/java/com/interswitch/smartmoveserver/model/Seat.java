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
@Table(name = "seats")
@EntityListeners(AuditingEntityListener.class)
public class Seat extends AuditEntity<String> implements Serializable {
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
