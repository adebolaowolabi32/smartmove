package com.interswitch.smartmoveserver.model;

public class Enum {
    public enum TransportMode {
        BUS, KEKE, RAIL, WATERWAY
    }

    public enum DeviceType {
        READER, VALIDATOR, READER_VALIDATOR
    }

    public enum DeviceStatus {
        CONNECTED, DISCONNECTED, BATTERY_LOW, EMERGENCY
    }

    public enum FareType {
        FIXED, VARIABLE
    }

    public enum TransactionType {
        CREDIT, DEBIT, TRIP, AGENT, CARD_DISABLED
    }

    public enum UserType {
        ISW_ADMIN, REGULATOR, OPERATOR, BUS_OWNER, AGENT
    }

    public enum CardType {
        ISW_ADMIN, REGULATOR, OPERATOR, BUS_OWNER, AGENT, EMV
    }

    public enum ConfigList {
        TRANSACTION_UPLOAD_PERIOD, GPS_UPLOAD_PERIOD
    }
}
