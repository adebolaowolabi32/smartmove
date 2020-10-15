package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.TicketTillSummary;
import com.interswitch.smartmoveserver.util.DateUtil;

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
