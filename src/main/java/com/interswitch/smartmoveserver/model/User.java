package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    @Column(unique=true)
    private String mobileNo;
    @Column(unique=true)
    private String email;
    //@ElementCollection
    private Enum.Role role;
    @ManyToOne
    @JoinColumn(name="parent_id")
    private User parent;
    private boolean isActive;
}
