package com.interswitch.smartmoveserver.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "devices")
public class Device {
    public static final int READER = 0;
    public static final int VALIDATOR = 1;
    public static final int READER_VALIDATOR = 2;
    public static final int FIXED = 0;
    public static final int VARIABLE = 1;

    @Id
    private String deviceId;
    private int type;
    private String ownerId;
    private String versionHardware;
    private String versionSoftware;
    private String versionEmv;
    private int fareType;
    private String periodTransactionUpload;
    private String periodGps;
    private String status;


    public enum DeviceType {
        READER, VALIDATOR, READER_VALIDATOR
    }

    public enum FareType {
        FIXED, VARIABLE
    }
}
