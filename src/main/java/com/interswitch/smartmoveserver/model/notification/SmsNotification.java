package com.interswitch.smartmoveserver.model.notification;

public class SmsNotification {

    private String sponsor;
    private String destinationAddress;
    private String sourceAddress;
    private String sourceNPI;
    private String type;
    private String destinationTON;
    private String transactionType;
    private String destinationNPI;
    private String routeId;
    private String service;
    private String text;
    private String sourceTON;
    private String requestContentType;
    private String applicationName;

    public SmsNotification(){}

    public SmsNotification(String destinationAddress, String text){
        this.destinationAddress = destinationAddress;
        this.text = text;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getSourceNPI() {
        return sourceNPI;
    }

    public void setSourceNPI(String sourceNPI) {
        this.sourceNPI = sourceNPI;
    }

    public String getDestinationTON() {
        return destinationTON;
    }

    public void setDestinationTON(String destinationTON) {
        this.destinationTON = destinationTON;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDestinationNPI() {
        return destinationNPI;
    }

    public void setDestinationNPI(String destinationNPI) {
        this.destinationNPI = destinationNPI;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSourceTON() {
        return sourceTON;
    }

    public void setSourceTON(String sourceTON) {
        this.sourceTON = sourceTON;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
