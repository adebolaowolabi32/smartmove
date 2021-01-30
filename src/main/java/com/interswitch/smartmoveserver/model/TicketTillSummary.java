package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket_till_summary")
@EntityListeners(AuditingEntityListener.class)
public class TicketTillSummary extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {

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

    @Override
    public Long getAuditableId() {
        return this.getId();
    }

    @Override
    public String getAuditableName() {
        return this.getClass().getSimpleName();
    }

}
