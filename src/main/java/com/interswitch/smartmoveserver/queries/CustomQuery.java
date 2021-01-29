package com.interswitch.smartmoveserver.queries;

public class CustomQuery {

    public static final String TICKET_TILL_BY_DATE_AND_STATUS_QUERY = "SELECT tt.till_operator_id as tillOperatorId, " +
            "tt.till_operator_name as tillOperatorName,sum(tt.total_amount) as totalSoldTicketValue, " +
            "tt.till_operator_owner_id as tillOperatorOwnerId, " +
            "count(tt.till_operator_name) as totalNumberOfTickets,ticket_issuance_date as ticketIssuanceDate, " +
            "min(ticket_issuance_time) as tillStartTime,max(ticket_issuance_time) as tillClosureTime " +
            "from ticket_till tt where  tt.ticket_issuance_date = :issuanceDate and tt.closed=:status " +
            "group by till_operator_id,till_operator_name,ticket_issuance_date,till_operator_owner_id";


    public static final String UPDATE_TICKET_TILL_BY_DATE_AND_OPERATOR_QUERY = "UPDATE ticket_till SET closed='TRUE' " +
            "where ticket_issuance_date =:issuanceDate AND till_operator_id =:tillOperatorId ";


    public static final String TICKET_TILL_STATUS_BY_DATE_AND_OPERATOR_ID = "SELECT tt.till_operator_id as tillOperatorId," +
            "tt.till_operator_name as tillOperatorName,sum(tt.total_amount) as totalSoldTicketValue, " +
            "tt.till_operator_owner_id as tillOperatorOwnerId, " +
            "count(tt.till_operator_name) as totalNumberOfTickets, " +
            "ticket_issuance_date as ticketIssuanceDate, " +
            "min(ticket_issuance_time) as tillStartTime, " +
            "max(ticket_issuance_time) as tillClosureTime " +
            "from ticket_till tt where  " +
            "tt.ticket_issuance_date = :issuanceDate and tt.till_operator_id= :tillOperatorId " +
            "group by till_operator_id,till_operator_name,ticket_issuance_date,till_operator_owner_id ";
}
