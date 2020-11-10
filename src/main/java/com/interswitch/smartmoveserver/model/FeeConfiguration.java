package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name="fee_configurations")
public class FeeConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotBlank(message="fee name is required")
    @Column(unique=true)
    private String feeName;

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
