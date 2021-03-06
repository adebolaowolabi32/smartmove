package com.interswitch.smartmoveserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "terminals")
@EntityListeners(AuditingEntityListener.class)
public class Terminal extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotBlank(message = "Name is required.")
    @Length(min = 5, max = 50, message = "Name must be between 5 and 30 characters long.")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Mode is required.")
    private Enum.TransportMode mode;

    @NotNull(message = "Country is required.")
    private String country;

    @NotNull(message = "State is required.")
    private String state;

    @NotBlank(message = "Code is required.")
    @Length(min = 2, max = 10, message = "Code must be between 2 and 10 characters long.")
    private String code;

    @NotNull(message = "LGA is required.")
    private String lga;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    private String location;

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