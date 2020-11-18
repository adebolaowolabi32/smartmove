package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String username;

    @Length(min = 5, max = 50, message = "Password must be between 5 and 30 characters long.")
    private String password;

    @NotBlank(message = "First name is required.")
    @Length(min = 5, max = 50, message = "First name must be between 3 and 30 characters long.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Length(min = 5, max = 50, message = "Last name must be between 3 and 30 characters long.")
    private String lastName;

    @Length(min = 5, max = 50, message = "Address must be between 8 and 50 characters long.")
    private String address;

    @Column(unique=true)
    @NotBlank(message = "Mobile number is required.")
    @Length(min = 5, max = 50, message = "Mobile number must be between 11 and 16 characters long.")
    private String mobileNo;

    @Column(unique=true)
    @NotBlank(message = "Email address is required.")
    @Length(min = 5, max = 50, message = "Email address must be between 4 and 30 characters long.")
    private String email;

    @Enumerated(EnumType.STRING)
    //@NotNull(message = "Role is required.")
    private Enum.Role role;

    @ManyToOne
    @JoinColumn(name="owner")
    private User owner;

    private boolean enabled;

    //@URL(message = "Picture URL is not valid")
    //@Length(max = 300, message = "Picture URL must be less than 300 characters long")
    private String pictureUrl;

    private transient MultipartFile picture;

    @Enumerated(EnumType.STRING)
    private Enum.TicketTillStatus tillStatus = Enum.TicketTillStatus.OPEN;
}