package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "routes")
@EntityListeners(AuditingEntityListener.class)
public class Route extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Mode is required.")
    private Enum.TransportMode mode;

    @Column(unique=true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "startTerminal")
    @NotNull(message = "Start terminal is required.")
    private Terminal startTerminal;

    @ManyToOne
    @JoinColumn(name = "stopTerminal")
    @NotNull(message = "Stop terminal is required.")
    private Terminal stopTerminal;

    @NotNull(message = "Fare is required.")
    private long fare;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private boolean enabled;

    @Override
    public Long getAuditableId() {
        return this.getId();
    }

    @Override
    public String getAuditableName() {
        return this.getClass().getSimpleName();
    }
}