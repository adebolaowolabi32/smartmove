package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String address;

    //@Column(unique=true)
    //add non null unique constraint
    private String mobileNo;

    //@Column(unique=true)
    //add non null unique constraint
    private String email;

    @Enumerated(EnumType.STRING)
    private Enum.Role role;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    private boolean enabled;
}
