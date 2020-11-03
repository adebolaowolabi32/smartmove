package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * @author adebola.owolabi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "terminals")
public class Terminal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required.")
    @Length(min = 5, max = 50, message = "Name must be between 5 and 30 characters long.")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transport mode is required.")
    private Enum.TransportMode mode;

    @NotNull(message = "Country is required.")
    private String country;

    @NotNull(message = "State is required.")
    private String state;

    private String code;

    @NotNull(message = "LGA is required.")
    private String lga;

    @ManyToOne
    @JoinColumn(name = "owner")
    @NotNull(message = "Owner is required.")
    private User owner;

    private boolean enabled;
}