package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 11/5/2020
 */
@Data
@Entity
@Table(name = "ticket_references")
@EntityListeners(AuditingEntityListener.class)
public class TicketReference extends  AuditEntity<String> implements Auditable<Long>,Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(min = 3, max = 10, message = "Prefix must be between 3 and 10 characters long.")
    private String prefix;

    @OneToOne(optional=false)
    private User owner;

    private boolean enabled;

    private boolean startTerminalEnabled;

    private boolean stopTerminalEnabled;

    @Override
    public Long getAuditableId() {
        return this.getId();
    }

    @Override
    public String getAuditableName() {
        return this.getClass().getSimpleName();
    }
}
