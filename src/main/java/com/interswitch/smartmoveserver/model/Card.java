package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "cards")
public class Card implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @Column(unique=true)
    private String pan;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiry;

    @Enumerated(EnumType.STRING)
    private Enum.CardType type;

    private long balance;

    private boolean enabled;
}
