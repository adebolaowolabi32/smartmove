package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.AuditableAction;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "audit_trails")
public class AuditTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String resource;
    private long resourceId;
    @Enumerated(EnumType.STRING)
    private AuditableAction action;
    private String actor;
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
    private Instant actionDate;
    private String actionTimeStamp;
    private String description;
    private String ipAddress;
    private String location;

}
