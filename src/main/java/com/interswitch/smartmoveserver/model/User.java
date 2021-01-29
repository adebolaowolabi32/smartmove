package com.interswitch.smartmoveserver.model;

import com.interswitchng.audit.model.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
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
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractAuditEntity<String> implements Auditable<Long>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String username;

    private String password;

    @NotBlank(message = "First name is required.")
    @Length(min = 2, max = 30, message = "First name must be between 2 and 30 characters long.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Length(min = 2, max = 30, message = "Last name must be between 2 and 30 characters long.")
    private String lastName;

    @Length(min = 5, max = 50, message = "Address must be between 5 and 50 characters long.")
    private String address;

    @Column(unique = true)
    @Length(min = 5, max = 30, message = "Mobile number must be between 5 and 30 characters long.")
    private String mobileNo;

    @Column(unique = true)
    @NotBlank(message = "Email address is required.")
    @Length(min = 5, max = 50, message = "Email address must be between 5 and 50 characters long.")
    private String email;

    @Enumerated(EnumType.STRING)
    //@NotNull(message = "Role is required.")
    private Enum.Role role;

    @ManyToOne
    @JoinColumn(name="owner")
    private User owner;

    private boolean enabled;

    //    @URL(message = "Picture URL is not valid")
//    @Length(max = 200, message = "Picture URL must be less than 200 characters long")
    private String pictureUrl;

    private transient MultipartFile picture;

    private transient int loginFreqType;

    @Enumerated(EnumType.STRING)
    private Enum.TicketTillStatus tillStatus = Enum.TicketTillStatus.OPEN;

    @Override
    public Long getAuditableId() {
        return this.getId();
    }

    @Override
    public String getAuditableName() {
        return this.getClass().getSimpleName();
    }
}