package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    @Column(unique=true)
    private String regNo;

    @Enumerated(EnumType.STRING)
    private Enum.TransportMode mode;

    @ManyToOne
    private VehicleCategory category;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @OneToOne
    private Device device;

    private String pictureUrl;

    private transient MultipartFile picture;

    private boolean enabled;
}
