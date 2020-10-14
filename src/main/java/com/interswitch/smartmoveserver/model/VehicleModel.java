package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 8/10/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicle_models")
public class VehicleModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private VehicleMake make;

    @Column(unique=true)
    private String name;

    private String year;
}
