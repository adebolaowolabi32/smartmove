package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Enum.TransportMode type;
    @Column(unique=true)
    private String name;
    @Column(unique=true)
    private String regNo;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToOne
    @JoinColumn(name = "device_id")
    private Device device;
    private boolean isActive;
    @ManyToMany(mappedBy = "vehicles")
    private Set<Route> routes;
}
