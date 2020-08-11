package com.interswitch.smartmoveserver.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "states")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String name;

    private String code;

    @ElementCollection(targetClass=String.class,fetch = FetchType.EAGER)
    private Set<String> localGovts;

}
