package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "buses")
public class Bus {
    @Id
    private String id;
    private String regNo;
    private String ownerId;
    private String deviceId;
}
