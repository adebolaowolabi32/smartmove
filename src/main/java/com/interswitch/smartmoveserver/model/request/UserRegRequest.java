package com.interswitch.smartmoveserver.model.request;

import com.interswitch.smartmoveserver.model.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegRequest extends UserRegistration {

    //@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{7,20}$",message="You")
    private String password;

    @NotBlank(message = "Full name is required.")
    @Length(min = 2, max = 30, message = "Full name name must be between 2 and 30 characters long.")
    private String fullName;

    @Length(min = 5, max = 30, message = "Mobile number must be between 5 and 30 characters long.")
    private String mobileNo;

    @NotBlank(message = "Email address is required.")
    @Length(min = 5, max = 50, message = "Email address must be between 5 and 50 characters long.")
    private String email;

    public User mapUserRequestToUser() {
        String firstName = this.getFullName().substring(0, this.getFullName().indexOf(" "));
        String lastName = this.getFullName().substring(this.getFullName().indexOf(" "));
        User user = User.builder()
                .password(this.getPassword())
                .address(this.getAddress())
                .role(this.getRole())
                .mobileNo(this.getMobileNo())
                .lastName(lastName)
                .firstName(firstName)
                .username(this.getEmail())
                .email(this.getEmail())
                .emailVerified(false)
                .build();
        return user;
    }
}
