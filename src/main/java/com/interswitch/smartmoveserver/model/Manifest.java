package com.interswitch.smartmoveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 7/18/2020
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "manifests")
public class Manifest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "seat")
    private Seat seat;

    @NotNull(message = "Seat is required.")
    private String seatNo;

    @NotBlank(message = "Name is required.")
    @Length(min = 5, max = 30, message = "Name must be between 5 and 30 characters long.")
    private String name;

    @NotBlank(message = "Address is required.")
    @Length(min = 5, max = 50, message = "Address must be between 5 and 50 characters long.")
    private String address;

    @NotNull(message = "Gender is required.")
    private String gender;

    @Length(max = 30, message = "BVN must be between 10 and 15 characters long.")
    private String bvn;

    @NotNull(message = "Nationality is required.")
    private String nationality;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "ID category is required.")
    private Enum.IdCategory idCategory;

    @Length(max = 30, message = "ID number must be between 5 and 20 characters long.")
    private String idNumber;

    @NotBlank(message = "Contact mobile is required.")
    @Length(min = 5, max = 30, message = "Contact mobile must be between 5 and 30 characters long.")
    private String contactMobile;

    @NotBlank(message = "Contact email is required.")
    @Length(min = 5, max = 30, message = "Contact email must be between 5 and 30 characters long.")
    private String contactEmail;

    @NotBlank(message = "Next of Kin name is required.")
    @Length(min = 5, max = 30, message = "Next of Kin name must be between 5 and 30 characters long.")
    private String nextOfKinName;

    @NotBlank(message = "Next of Kin mobile is required.")
    @Length(min = 5, max = 30, message = "Next of Kin mobile must be between 5 and 30 characters long.")
    private String nextOfKinMobile;

    @ManyToOne
    @JoinColumn(name = "trip")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "schedule")
    @NotNull(message = "Schedule is required.")
    private Schedule schedule;

    private boolean boarded;
    private boolean completed;
    private LocalDateTime timeofBoarding;
    private LocalDateTime timeofCompletion;
}