package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket_till_summary")
public class TicketTillSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long tillOperatorId;

    private String tillOperatorName;

    private long tillOperatorOwner;

    private long totalSoldTickets;

    private double totalSoldAmount;

    private boolean approved;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private String tillStartTime;

    private String tillEndTime;

    @ManyToOne
    @JoinColumn(name = "approver")
    private User approver;

}
