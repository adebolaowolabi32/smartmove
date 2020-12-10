package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "routes")
@EntityListeners(AuditingEntityListener.class)
public class Route extends AbstractAuditEntity<String> implements Auditable<Long>,Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is required.")
    private Enum.TransportMode type;

    @Column(unique=true)
    @NotBlank(message = "Name is required.")
    @Length(min = 5, max = 50, message = "Name must be between 5 and 30 characters long.")
    private String name;

    @NotNull(message = "Start terminal is required.")
    private Long startTerminalId;

    private String startTerminalName;

    @NotNull(message = "Stop terminal is required.")
    private Long stopTerminalId;

    private String stopTerminalName;

    @Positive(message = "only positive values allowed for price")
    private long price;

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