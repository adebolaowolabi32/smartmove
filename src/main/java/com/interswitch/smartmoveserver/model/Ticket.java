package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interswitchng.audit.model.Auditable;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 7/25/2020
 */
@Data
@Entity
@Table(name = "tickets")
@EntityListeners(AuditingEntityListener.class)
public class Ticket extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String referenceNo;

    private String paymentReferenceNo;

    private String passengerName;

    @ManyToOne
    @JoinColumn(name = "operator")
    private User operator;

    @ManyToOne
    @JoinColumn(name = "trip")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "schedule")
    private Schedule schedule;

    private double fare;

    @Enumerated(EnumType.STRING)
    private Enum.SeatClass seatClass;

    @JoinColumn(name = "seat")
    private Seat seat;

    private String seatNo;

    private String bookingDate;

    private boolean refunded = false;

    @JsonIgnore
    @Override
    public Long getAuditableId() {
        return this.getId();
    }

    @JsonIgnore
    @Override
    public String getAuditableName() {
        return this.getClass().getSimpleName();
    }
}
