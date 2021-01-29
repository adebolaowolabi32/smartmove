package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Set;

/*
 * Created by adebola.owolabi on 8/10/2020
 */
@Data
@Entity
@Table(name = "vehicle_categories")
@EntityListeners(AuditingEntityListener.class)
public class VehicleCategory extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Mode is required.")
    private Enum.TransportMode mode;

    @Column(unique=true)
    @NotBlank(message = "Name is required.")
    @Length(min = 5, max = 50, message = "Name must be between 5 and 30 characters long.")
    private String name;

    @OneToOne
    @NotNull(message = "Make is required.")
    private VehicleMake make;

    @OneToOne
    @NotNull(message = "Model is required.")
    private VehicleModel model;

    @NotNull(message = "Colour is required.")
    private String color;

/*    @ManyToMany
    private List<Facility> facilities;*/

    @URL(message = "Picture URL is not valid")
    @Length(max = 300, message = "Picture URL must be less than 300 characters long")
    private String pictureUrl;

    @JsonIgnore
    private transient MultipartFile picture;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @JsonIgnore
    private int noRows;

    @JsonIgnore
    private int noColumns;

    @NotNull(message = "Capacity is required.")
    private int capacity;

    @Column(name = "seats", nullable = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "seat")
    private Set<Seat> seats;

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