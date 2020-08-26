package com.interswitch.smartmoveserver.model.request;

import lombok.Data;

@Data
public class Transfer {
    private String to;
    private double amount;
}
