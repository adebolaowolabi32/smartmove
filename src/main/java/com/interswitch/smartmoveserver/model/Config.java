package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Name is required.")
    private Enum.ConfigList name;

    @NotBlank(message = "Value is required.")
    private String value;
}