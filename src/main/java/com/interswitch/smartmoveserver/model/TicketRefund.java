package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 7/25/2020
 */
@Data
@Entity
@Table(name = "ticket_refunds")
public class TicketRefund implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Ticket ticket;

    private String reason;

    private LocalDateTime refundDateTime;

    @ManyToOne
    private User operator;
}
