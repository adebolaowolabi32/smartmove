package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

/*
 * Created by adebola.owolabi on 11/5/2020
 */
@Data
@Entity
@Table(name = "user_approvals")
public class UserApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(optional = false)
    private User usr;

    @ManyToOne
    private User owner;

    private boolean approved;
}
