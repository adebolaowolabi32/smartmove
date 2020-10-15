package com.interswitch.smartmoveserver.repository;
import com.interswitch.smartmoveserver.model.TicketTill;
import com.interswitch.smartmoveserver.model.view.TicketTillView;
import com.interswitch.smartmoveserver.queries.CustomQuery;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TicketTillRepository extends CrudRepository<TicketTill, Long> {

    @Query(value = CustomQuery.TICKET_TILL_BY_DATE_AND_STATUS_QUERY, nativeQuery = true)
    List<TicketTillView> findAggregatedTicketTillByIssuanceDateAndStatus(
            @Param("issuanceDate") String issuanceDate, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query(value = CustomQuery.UPDATE_TICKET_TILL_BY_DATE_AND_OPERATOR_QUERY, nativeQuery = true)
    void updateTicketTillStatusByTillOperatorIdAndIssuanceDate(@Param("issuanceDate") String issuanceDate, @Param("tillOperatorId") long tillOperatorId);

    @Query(value = CustomQuery.TICKET_TILL_STATUS_BY_DATE_AND_OPERATOR_ID, nativeQuery = true)
    TicketTillView findCurrentTicketTillStatusByDateAndTillOperatorId(
            @Param("issuanceDate") String issuanceDate, @Param("tillOperatorId") long tillOperatorId);
}
