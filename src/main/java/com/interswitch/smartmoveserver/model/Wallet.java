package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interswitchng.audit.model.Auditable;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "wallets")
@EntityListeners(AuditingEntityListener.class)
public class Wallet extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @OneToOne
    private User owner;

    private double balance = 0;

    private boolean enabled;

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
