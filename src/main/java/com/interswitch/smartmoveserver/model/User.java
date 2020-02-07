package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String emailAddress;
    private String parent;
    private UserType type;

    public enum UserType {
        ISW_ADMIN, REGULATOR, OPERATOR, BUS_OWNER, AGENT
    }
}
