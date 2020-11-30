package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import com.interswitchng.audit.model.AuditableAction;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "audit_records")
public class AuditRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String resource;
    private long resourceId;
    @Enumerated(EnumType.STRING)
    private AuditableAction action;
    private String actor;
    private Instant actionDate;
    private String description;
}
