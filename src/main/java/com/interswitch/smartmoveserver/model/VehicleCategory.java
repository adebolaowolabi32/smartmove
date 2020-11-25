package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 8/10/2020
 */
@Data
@Entity
@Table(name = "vehicle_categories")
public class VehicleCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transport mode is required.")
    private Enum.TransportMode mode;

    @Column(unique=true)
    @NotBlank(message = "Name is required.")
    @Length(min = 5, max = 50, message = "Name must be between 5 and 30 characters long.")
    private String name;

    @OneToOne
    @NotNull(message = "Make is required.")
    private VehicleMake make;

    @OneToOne
    @NotNull(message = "Model is required.")
    private VehicleModel model;

    @NotNull(message = "Colour is required.")
    private String color;

/*    @ManyToMany
    private List<Facility> facilities;*/

    private String pictureUrl;

    private transient MultipartFile picture;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private int noRows;

    private int noColumns;

    @NotNull(message = "Capacity is required.")
    private int capacity;
}