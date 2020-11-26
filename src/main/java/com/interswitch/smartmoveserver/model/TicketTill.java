package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket_till")
@EntityListeners(AuditingEntityListener.class)
public class TicketTill extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private long ticketId;

    private String tillOperatorUsername;

    @Column(nullable = true)
    private long tillOperatorId;

    private String tillOperatorName;

    private double totalAmount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ticketIssuanceDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime ticketIssuanceTime;

    private boolean approved;

    @Column(nullable = true)
    private boolean closed;

    private long tillOperatorOwnerId;
}

