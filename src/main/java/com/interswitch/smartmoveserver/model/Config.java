package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "systemConfigurations")
@EntityListeners(AuditingEntityListener.class)
public class Config extends AbstractAuditEntity<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Name is required.")
    private Enum.ConfigList name;

    @NotBlank(message = "Value is required.")
    private String value;
}