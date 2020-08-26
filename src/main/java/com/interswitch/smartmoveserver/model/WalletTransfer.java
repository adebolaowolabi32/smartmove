package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 5/19/2020
 */
@Data
@Entity
@Table(name = "wallet_transfers")
public class WalletTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Wallet wallet;

    private String recipient;

    private double amount;

    private LocalDateTime transferDateTime;
}
