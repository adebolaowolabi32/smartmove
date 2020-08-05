package com.interswitch.smartmoveserver.model;

import lombok.Data;

import java.io.Serializable;

/*
 * Created by adebola.owolabi on 3/25/2020.
 */
@Data
public class Participant implements Serializable {
    private Integer id;

    private String name;

    private String code;

    private String role;

    private double percentage;

    private String schemeName;

    private Integer schemeId;

    private Integer settlementRequestId;
}
