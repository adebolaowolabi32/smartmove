package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Enum.TransportMode type;
    @Column(unique=true)
    private String name;
    private String start;
    private String stop;
    private long price;
    @ManyToOne
    private User owner;
    private boolean isActive;
    @ManyToMany
    private Set<Vehicle> vehicles;
}
