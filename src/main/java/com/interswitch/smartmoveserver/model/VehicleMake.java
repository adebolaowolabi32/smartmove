package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interswitchng.audit.model.Auditable;
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
@Table(name = "vehicle_makes")
@EntityListeners(AuditingEntityListener.class)
public class VehicleMake extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    @JsonIgnore
    @Override
    public Long getAuditableId() {
        return this.getId();
    }

    @JsonIgnore
    @Override
    public String getAuditableName() {
        return this.getClass().getSimpleName();
    }
}
