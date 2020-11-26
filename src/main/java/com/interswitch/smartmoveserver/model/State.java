package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/*
 * Created by earnest.suru on 8/10/2020
 */
@Data
@Entity
@Table(name = "states")
public class State implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    private String code;

    @ElementCollection(targetClass=String.class)
    private List<String> localGovts;

}
