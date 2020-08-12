package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    @Column(unique=true)
    private String regNo;

    @ManyToOne
    private VehicleCategory category;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @OneToOne
    private Device device;

    private boolean enabled;
}
