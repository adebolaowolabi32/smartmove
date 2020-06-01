package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

/*
 * Created by adebola.owolabi on 5/21/2020
 */
@Data
@Entity
@Table(name = "blacklists")
public class Blacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String identifier;

    private Enum.ItemType type;
}
