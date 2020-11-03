package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 5/21/2020
 */
@Data
@Entity
@Table(name = "blacklists")
public class Blacklist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Identifier is Required")
    @Length(min = 5, max = 50, message = "Identifier must be between 5 and 30 characters long")
    private String identifier;

    @NotNull(message = "Type must not be null")
    private Enum.ItemType type;
}