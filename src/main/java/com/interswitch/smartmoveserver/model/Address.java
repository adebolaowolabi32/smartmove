package com.interswitch.smartmoveserver.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(name = "state")
    private String state;
    @Column(name = "lga")
    private String lga;
    @Column(name = "description")
    private String description;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "latitude")
    private String latitude;
}
