package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 5/21/2020
 */
@Data
@Entity
@Table(name = "blacklists")
public class Blacklist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String identifier;

    private Enum.ItemType type;
}
