package com.interswitch.smartmoveserver.model;

/**
 * @author adebola.owolabi
 */
public class Enum {
    public enum TransportMode {
        KEKE, BUS, CAR, RAIL, FERRY, RICKSHAW
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
        CREDIT, DEBIT, TAP_IN, TAP_OUT, LOAD_CARD, DISABLE_CARD, CHECK_BALANCE
    }

    public enum Role {
        ISW_ADMIN, REGULATOR, OPERATOR, VEHICLE_OWNER, AGENT, DRIVER, COMMUTER
    }

    public enum CardType {
        ISW_ADMIN, REGULATOR, OPERATOR, VEHICLE_OWNER, EMV, DRIVER, AGENT, COMMUTER
    }

    public enum ItemType {
        CARD, DEVICE, VEHICLE, USER
    }

    public enum ConfigList {
        TRANSACTION_UPLOAD_PERIOD, GPS_UPLOAD_PERIOD
    }

    public enum SeatClass {
        STANDARD, PREMIUM, LUXURY
    }
}
