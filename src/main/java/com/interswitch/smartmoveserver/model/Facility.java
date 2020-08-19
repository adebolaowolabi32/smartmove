package com.interswitch.smartmoveserver.model;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 8/11/2020
 */
@Data
@Entity
@Table(name = "facilities")
public class Facility implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    private String image;
/*
    @ManyToMany
    private List<VehicleCategory> vehicleCategories;*/

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

}
