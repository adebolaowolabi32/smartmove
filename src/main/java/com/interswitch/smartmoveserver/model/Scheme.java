package com.interswitch.smartmoveserver.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

/*
 * Created by adebola.owolabi on 4/21/2020
 */
@Data
public class Scheme implements Serializable {
    private Integer id;

    private String name;

    private LocalTime settlementTime = LocalTime.of(18, 0);

    private String owner;
}
