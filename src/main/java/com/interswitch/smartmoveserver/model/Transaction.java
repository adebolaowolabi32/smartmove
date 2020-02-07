package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    public static final int CREDIT = 0;
    public static final int DEBIT = 1;
    public static final int TRIP = 2;
    public static final int AGENT = 3;
    public static final int CARD_DISABLED = 4;


    @Id
    private String id;
    private String deviceId;
    private String cardNumber;
    private String amount;
    private int type;
    private String timeDate;

    public enum TransactionType {
        CREDIT, DEBIT, TRIP, AGENT, CARD_DISABLED
    }
}
