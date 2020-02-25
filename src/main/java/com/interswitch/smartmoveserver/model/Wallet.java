package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private User owner;
    private String currency;
    private double balance;
    private boolean isActive;
}
