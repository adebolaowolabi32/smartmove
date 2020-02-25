package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    @Column(unique=true)
    private String mobileNo;
    @Column(unique=true)
    private String email;
    private Enum.UserType type;
    @ManyToMany
    @JoinColumn(name = "id")
    private Set<Role> roles;
    @ManyToOne
    private User parent;
    private boolean isActive;
}
