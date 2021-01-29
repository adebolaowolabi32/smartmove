package com.interswitch.smartmoveserver.model.request;

import com.interswitch.smartmoveserver.model.Enum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
public class UserRegistration implements Serializable {

    @Length(min = 5, max = 50, message = "Address must be between 5 and 50 characters long.")
    private String address;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required.")
    private Enum.Role role;

    private String owner;
}