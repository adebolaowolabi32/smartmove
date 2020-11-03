package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Type is required.")
    private Enum.TransportMode type;

    @Column(unique=true)
    @NotBlank(message = "Name is required.")
    @Length(min = 5, max = 50, message = "Name must be between 5 and 30 characters long.")
    private String name;

    @NotNull(message = "Start terminal is required.")
    private Long startTerminalId;

    private String startTerminalName;

    @NotNull(message = "Stop terminal is required.")
    private Long stopTerminalId;

    private String stopTerminalName;

    @NotBlank(message = "Price is required.")
    private long price;

    @ManyToOne
    @JoinColumn(name = "owner")
    @NotNull(message = "Owner is required.")
    private User owner;

    private boolean enabled;
}