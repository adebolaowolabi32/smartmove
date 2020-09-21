package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.Ticket;
import lombok.Data;

import java.io.Serializable;


/*
 * Created by adebola.owolabi on 9/15/2020
 */
@Data
public class RefundTicket implements Serializable {
    private String referenceNo;

    private Ticket ticket;

    private String reason;

    private boolean invalid;
}
