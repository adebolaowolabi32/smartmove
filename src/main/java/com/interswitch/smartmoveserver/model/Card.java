package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author adebola.owolabi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cards")
@EntityListeners(AuditingEntityListener.class)
public class Card extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @Column(unique=true)
    @NotBlank(message = "PAN is required.")
    @Length(min = 5, max = 50, message = "PAN must be between 14 and 30 characters long.")
    private String pan;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Expiry date is required.")
    @Future
    private LocalDate expiry;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is required.")
    private Enum.CardType type;

    private long balance;

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