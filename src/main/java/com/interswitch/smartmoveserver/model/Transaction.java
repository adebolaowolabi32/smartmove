package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String cardNumber;
    @ManyToOne
    private Device device;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User recipient;
    private double amount;
    private Enum.TransactionType type;
    private Date timeStamp;
}
