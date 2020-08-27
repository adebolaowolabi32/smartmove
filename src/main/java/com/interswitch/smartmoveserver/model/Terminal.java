package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "terminals")
public class Terminal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode mode;

    private String country;

    private String state;

    private String code;

    private String lga;

    private String location;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private boolean enabled;
}