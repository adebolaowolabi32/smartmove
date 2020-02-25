package com.interswitch.smartmoveserver.model.request;

import lombok.Data;

@Data
public class Transfer {
    private long from;
    private long to;
    private double amount;
}
