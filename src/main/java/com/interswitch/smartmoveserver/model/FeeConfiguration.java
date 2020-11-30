package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name="fee_configurations")
@EntityListeners(AuditingEntityListener.class)
public class FeeConfiguration extends AuditEntity<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Enum.FeeName feeName;

    @NotNull(message="fee value is required")
    @Positive(message="fee value can only be a positive number")
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

}
