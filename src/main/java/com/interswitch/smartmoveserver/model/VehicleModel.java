package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 8/10/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicle_models")
@EntityListeners(AuditingEntityListener.class)
public class VehicleModel extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private VehicleMake make;

    @Column(unique=true)
    private String name;

    private String year;
}
