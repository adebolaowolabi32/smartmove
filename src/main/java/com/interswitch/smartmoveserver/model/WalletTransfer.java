package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

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

    @OneToOne
    private User recipient;

    private double amount;
}
