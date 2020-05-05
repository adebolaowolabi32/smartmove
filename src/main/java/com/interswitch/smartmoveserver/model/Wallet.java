package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User owner;

    private double balance = 0;

    private boolean enabled;
}
