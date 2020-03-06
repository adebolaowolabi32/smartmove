package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique=true)
    private String name;
    private String deviceId;
    private Enum.DeviceType type;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    private String hardwareVersion;
    private String softwareVersion;
    private Enum.FareType fareType;
    private Enum.DeviceStatus deviceStatus;
    private boolean isActive;
}
