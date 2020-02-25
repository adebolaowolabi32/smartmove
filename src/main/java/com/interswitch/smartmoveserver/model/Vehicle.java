package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

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
    private User owner;
    @OneToOne
    @JoinColumn(name = "id")
    private Device device;
    private boolean isActive;
    @ManyToMany(mappedBy = "vehicles")
    private Set<Route> routes;
}
