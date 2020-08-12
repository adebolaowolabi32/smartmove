package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "routes")
public class Route implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode type;

    @Column(unique=true)
    private String name;

    private Long startTerminalId;

    private String startTerminalName;

    private Long stopTerminalId;

    private String stopTerminalName;

    private long price;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private boolean enabled;
}
