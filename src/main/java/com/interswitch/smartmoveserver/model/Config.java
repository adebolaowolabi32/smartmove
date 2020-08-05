package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "systemConfigurations")
public class Config implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    @Enumerated(EnumType.STRING)
    private Enum.ConfigList name;

    private String value;
}
