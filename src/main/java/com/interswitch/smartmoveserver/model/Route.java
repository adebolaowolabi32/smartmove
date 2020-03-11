package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode type;

    @Column(unique=true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "start_id")
    private Terminal start;

    @ManyToOne
    @JoinColumn(name = "stop_id")
    private Terminal stop;

    private long price;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private boolean enabled;

    @ManyToMany
    @JoinColumn(name = "vehicle_id")
    private Set<Vehicle> vehicles;
}
