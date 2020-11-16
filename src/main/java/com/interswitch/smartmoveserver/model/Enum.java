package com.interswitch.smartmoveserver.model;

/**
 * @author adebola.owolabi
 */
public class Enum {
    public enum TransportMode {
        BUS, CAR, RAIL, FERRY, RICKSHAW
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
        CREDIT, DEBIT, TAP_IN, TAP_OUT, LOAD_CARD, DISABLE_CARD, CHECK_BALANCE, TICKET_SALE
    }

    public enum Role {
        ISW_ADMIN, REGULATOR, OPERATOR, EXECUTIVE, SERVICE_PROVIDER, INSPECTOR, TICKETER, VEHICLE_OWNER, AGENT, DRIVER, COMMUTER
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

    public enum PaymentMode {
        CASH, POS, TRANSFER
    }

    public enum IdCategory {
        NATIONAL_ID, DRIVERS_LICENSE, INTERNATIONAL_PASSPORT, VOTERS_CARD, SCHOOL_ID, OTHER, NO_ID
    }

    public enum TicketTillStatus{
        OPEN,CLOSE
    }

    public enum RatingMetricType{
        PERCENT,FLAT
    }

    public enum FeeName{
        VAT("VAT"), ID_CARD_FEE("ID CARD Fee");

        private String name;

        FeeName(String name) {
            this.name = name;
        }

        public String getCustomName() {
            return name;
        }

    }
}
