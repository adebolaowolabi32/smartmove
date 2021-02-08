
package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Created by adebola.owolabi on 7/18/2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seats")
@EntityListeners(AuditingEntityListener.class)
public class Seat extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int seatNo;

    @JsonIgnore
    private int rowNo;

    @JsonIgnore
    private int columnNo;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Enum.SeatClass seatClass;

    private boolean available;

    @JsonIgnore
    private boolean picked;

    @JsonIgnore
    @ManyToOne
    @NotNull
    private VehicleCategory vehicle;

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
