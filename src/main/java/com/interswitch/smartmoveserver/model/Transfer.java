package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 5/19/2020
 */
@Data
@Entity
@Table(name = "transfers")
@EntityListeners(AuditingEntityListener.class)
public class Transfer extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Wallet wallet;

    private String recipient;

    private double amount;

    @DateTimeFormat(pattern = "MMM dd yyyy HH:mm aa")
    private LocalDateTime transferDateTime;

    @ManyToOne
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
