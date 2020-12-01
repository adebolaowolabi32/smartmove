package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.interswitchng.audit.model.Auditable;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
public class Transaction extends AbstractAuditEntity<String> implements Auditable<Long>,Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String transactionId = UUID.randomUUID().toString();

    private String cardId;

    private String deviceId;

    @NotNull(message = "Terminal ID is required.")
    private String terminalId;

    @NotNull(message = "Vehicle ID is required.")
    private String vehicleId;

    @NotNull(message = "Operator ID is required.")
    private String operatorId;

    @NotNull(message = "Amount is required.")
    private double amount;

    @ManyToOne
    private User owner;
/*
    private String walletBalance;

    private String cardBalance;*/

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transaction type is required.")
    private Enum.TransactionType type;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transport mode is required.")
    private Enum.TransportMode mode;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Transaction date time is required.")
    private LocalDateTime transactionDateTime;
    private String schemeName;


    private Integer schemeId;

    @Override
    public Long getAuditableId() {
        return this.getId();
    }

    @Override
    public String getAuditableName() {
        return this.getClass().getSimpleName();
    }
}