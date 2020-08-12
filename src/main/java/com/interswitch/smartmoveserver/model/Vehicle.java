package com.interswitch.smartmoveserver.model;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

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

    private String vehicleId;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode type;

    @Column(unique=true)
    private String name;

    @Column(unique=true)
    private String regNo;

    private String model;

    //TODO:: add to frontend
    private String color;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne
    @JoinColumn(name = "device_id")
    private Device device;

    private boolean enabled;

    @ManyToMany(mappedBy = "vehicles")
    private Set<Route> routes;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "document_id")
    private Document vehiclePicture;

    private int noRows;

    private int noColumns;

    private int noSeats;

    @Column(name="facilities")
    @ElementCollection(targetClass=String.class)
    private Set<String> facilities;
}
