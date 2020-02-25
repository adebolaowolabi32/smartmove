package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "terminals")
public class Terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique=true)
    private String name;
    private Enum.TransportMode type;
    private String location;
    @ManyToOne
    private User owner;
    private boolean isActive;
}