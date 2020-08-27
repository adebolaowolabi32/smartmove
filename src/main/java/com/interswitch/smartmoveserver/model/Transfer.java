package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
 * Created by adebola.owolabi on 5/19/2020
 */
@Data
@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Wallet wallet;

    private String recipient;

    private double amount;

    @DateTimeFormat(pattern = "MMM dd yyyy HH:mm aa")
    private LocalDateTime transferDateTime;
}