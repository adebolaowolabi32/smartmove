package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private String mobileNo;

    private String email;

    @Enumerated(EnumType.STRING)
    private Enum.Role role;

    @ManyToOne
    @JoinColumn(name="owner")
    private User owner;

    private boolean enabled;

    private String pictureUrl;

    private transient MultipartFile picture;

    @Enumerated(EnumType.STRING)
    private Enum.TicketTillStatus tillStatus = Enum.TicketTillStatus.OPEN;;
}
