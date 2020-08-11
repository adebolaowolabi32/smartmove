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

    @Column(unique=true)
    private String name;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode type;

    private String location;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private boolean enabled;
}