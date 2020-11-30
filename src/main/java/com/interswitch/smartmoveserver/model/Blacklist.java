package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "blacklists")
@EntityListeners(AuditingEntityListener.class)
public class Blacklist extends AuditEntity<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Identifier is Required")
    @Length(min = 5, max = 50, message = "Identifier must be between 5 and 30 characters long")
    private String identifier;

    @NotNull(message = "Type must not be null")
    private Enum.ItemType type;
}