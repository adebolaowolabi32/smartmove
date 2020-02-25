package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @ManyToMany
    @JoinColumn(name = "id")
    private Set<Permission> permissions;
}