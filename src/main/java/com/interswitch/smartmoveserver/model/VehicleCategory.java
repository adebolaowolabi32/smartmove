package com.interswitch.smartmoveserver.model;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 8/10/2020
 */
@Data
@Entity
@Table(name = "vehicle_categories")
public class VehicleCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode mode;

    @Column(unique=true)
    private String name;

    @OneToOne
    private VehicleMake make;

    @OneToOne
    private VehicleModel model;

    private String color;

/*    @ManyToMany
    private List<Facility> facilities;*/

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private int noRows;

    private int noColumns;

    private int capacity;
}
