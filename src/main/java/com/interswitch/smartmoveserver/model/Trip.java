package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 7/17/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "trips")
@EntityListeners(AuditingEntityListener.class)
public class Trip extends AbstractAuditEntity<String> implements Auditable<Long>,Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String referenceNo;

    private long fare;

    @ManyToOne
    @JoinColumn(name = "driver")
    @NotNull(message = "Driver is required.")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "schedule")
    @NotNull(message = "Schedule is required.")
    private Schedule schedule;

    @ManyToOne
    @NotNull(message = "Vehicle is required.")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @Override
    public Long getAuditableId() {
        return this.getId();
    }

    @Override
    public String getAuditableName() {
        return this.getClass().getSimpleName();
    }
}