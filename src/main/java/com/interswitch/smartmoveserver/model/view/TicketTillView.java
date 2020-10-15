package com.interswitch.smartmoveserver.model.view;

public interface TicketTillView {

    String getTillOperatorName();

    double getTotalSoldTicketValue();

    long getTotalNumberOfTickets();

    String getTicketIssuanceDate();

    String getTillStartTime();

    String getTillClosureTime();

    long getTillOperatorId();

    long getTillOperatorOwnerId();
}
