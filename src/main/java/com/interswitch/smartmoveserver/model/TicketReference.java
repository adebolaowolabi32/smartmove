package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/*
 * Created by adebola.owolabi on 11/5/2020
 */
@Data
@Entity
@Table(name = "ticket_references")
public class TicketReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(min = 3, max = 10, message = "Prefix must be between 3 and 10 characters long.")
    private String prefix;

    @OneToOne(optional=false)
    private User owner;

    private boolean enabled;

    private boolean startTerminalEnabled;

    private boolean stopTerminalEnabled;
}
