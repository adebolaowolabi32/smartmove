package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String transactionId;

    private String cardId;

    private String deviceId;

    private String terminalId;

    private String vehicleId;

    private String routeId;

    private String agentId;

    private String operatorId;

    private double amount;

    private String walletBalance;

    private String cardBalance;

    @Enumerated(EnumType.STRING)
    private Enum.TransactionType type;

    private Enum.TransportMode mode;

    private LocalDateTime transactionDateTime;
}