package com.interswitch.smartmoveserver.model.view;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeDetails implements Serializable {
    private String feeName;
    private double amount;
}
