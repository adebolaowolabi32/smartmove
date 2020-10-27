package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String transactionId = UUID.randomUUID().toString();

    @NotNull(message = "Card ID is required.")
    private String cardId;

    @NotNull(message = "Device ID is required.")
    private String deviceId;

    @NotNull(message = "Terminal ID is required.")
    private String terminalId;

    @NotNull(message = "Vehicle ID is required.")
    private String vehicleId;

    @NotNull(message = "Operator ID is required.")
    private String operatorId;

    @NotNull(message = "Amount is required.")
    private double amount;
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
}