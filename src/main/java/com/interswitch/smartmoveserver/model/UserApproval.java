package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 11/5/2020
 */
@Data
@Entity
@Table(name = "user_approvals")
public class UserApproval extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(optional = false)
    private User usr;

    @ManyToOne
    private User owner;

    private boolean approved;

    private Enum.SignUpType signUpType;

    @Override
    public Long getAuditableId() {
        return this.getId();
    }

    @Override
    public String getAuditableName() {
        return this.getClass().getSimpleName();
    }

}
