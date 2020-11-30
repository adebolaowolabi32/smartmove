package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "vehicles")
@EntityListeners(AuditingEntityListener.class)
public class Vehicle extends  AuditEntity<String> implements Auditable<Long>,Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    @NotBlank(message = "Name is required.")
    @Length(min = 5, max = 50, message = "Name must be between 5 and 30 characters long.")
    private String name;

    @Column(unique=true)
    @NotBlank(message = "Registration number is required.")
    @Length(min = 5, max = 50, message = "Registration number must be between 5 and 30 characters long.")
    private String regNo;

    @ManyToOne
    @NotNull(message = "Vehicle category is required.")
    private VehicleCategory category;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @OneToOne
    private Device device;

    @URL(message = "Picture URL is not valid")
    @Length(max = 300, message = "Picture URL must be less than 300 characters long")
    private String pictureUrl;

    private transient MultipartFile picture;

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