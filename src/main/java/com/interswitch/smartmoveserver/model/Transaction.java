package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String transactionId = UUID.randomUUID().toString();

    private String cardId;

    private String deviceId;

    private String terminalId;

    private String vehicleId;

    private String routeId;

    private String agentId;

    private String operatorId;

    private double amount;
/*
    private String walletBalance;

    private String cardBalance;*/

    @Enumerated(EnumType.STRING)
    private Enum.TransactionType type;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode mode;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDateTime;

    private String schemeName;

    private Integer schemeId;
}