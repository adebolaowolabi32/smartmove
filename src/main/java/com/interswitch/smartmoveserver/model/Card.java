package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Owner is required.")
    private User owner;

    @Column(unique=true)
    @NotBlank(message = "PAN is required.")
    @Length(min = 5, max = 50, message = "PAN must be between 14 and 30 characters long.")
    private String pan;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Expiry date is required.")
    @Future
    private LocalDate expiry;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is required.")
    private Enum.CardType type;

    private long balance;

    private boolean enabled;
}