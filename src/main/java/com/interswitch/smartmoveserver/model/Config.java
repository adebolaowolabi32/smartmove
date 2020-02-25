package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "systemConfigurations")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique=true)
    private Enum.ConfigList name;
    private String value;
}
