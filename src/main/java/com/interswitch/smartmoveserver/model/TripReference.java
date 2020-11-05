package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/*
 * Created by adebola.owolabi on 11/5/2020
 */
@Data
@Entity
@Table(name = "trip_references")
public class TripReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(max = 10, message = "Prefix must be less than 10 characters long.")
    private String prefix;

    @OneToOne(optional=false)
    private User owner;

    private boolean enabled;
}
