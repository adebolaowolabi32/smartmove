package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "devices")
@EntityListeners(AuditingEntityListener.class)
public class Device extends AuditEntity<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    @NotBlank(message = "Name is required.")
    @Length(min = 5, max = 50, message = "Name must be between 5 and 30 characters long.")
    private String name;

    @Column(unique=true)
    @NotBlank(message = "Device ID is required.")
    @Length(min = 5, max = 50, message = "Device ID must be between 14 and 30 characters long.")
    private String deviceId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Device type is required.")
    private Enum.DeviceType type;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @NotBlank(message = "Hardware version is required.")
    @Length(max = 30, message = "Hardware version must be less than 30 characters long.")
    private String hardwareVersion;

    @NotBlank(message = "Software version is required.")
    @Length(max = 30, message = "Software version must be less than 30 characters long.")
    private String softwareVersion;

    private int batteryPercentage;

    private double balance = 0;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Fare type is required.")
    private Enum.FareType fareType;

    @Enumerated(EnumType.STRING)
    private Enum.DeviceStatus deviceStatus;

    private boolean enabled;
}