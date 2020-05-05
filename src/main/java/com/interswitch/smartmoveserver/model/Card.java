package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(unique=true)
    private String pan;

    private Date expiry;

    @Enumerated(EnumType.STRING)
    private Enum.CardType type;

    private long balance;

    private boolean enabled;
}
