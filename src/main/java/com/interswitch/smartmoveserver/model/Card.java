package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne
    private User owner;
    @Column(unique=true)
    private String pan;
    private Date expiry;
    private Enum.CardType type;
    private long balance;
    private boolean isActive;
}
