package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
public class SystemTimings {
    private String periodTransactionUpload;
    private String periodGPS;
}
