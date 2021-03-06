package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "fee_configurations")
@EntityListeners(AuditingEntityListener.class)
public class FeeConfiguration extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Enum.FeeName feeName;

    @NotNull(message = "fee value is required")
    @Positive(message = "fee value can only be a positive number")
    private double value;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Rating type is required.")
    private Enum.RatingMetricType ratingMetricType;

    private String description;

    @ManyToOne
    @JoinColumn(name = "operator")
    private User operator;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

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
