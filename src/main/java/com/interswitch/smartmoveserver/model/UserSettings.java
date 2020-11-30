package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/*
 * Created by adebola.owolabi on 11/18/2020
 */
@Data
@Entity
@Table(name = "user_settings")

@EntityListeners(AuditingEntityListener.class)
public class UserSettings extends AuditEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(optional = false)
    private User owner;

    private boolean makerCheckerEnabled;
}
