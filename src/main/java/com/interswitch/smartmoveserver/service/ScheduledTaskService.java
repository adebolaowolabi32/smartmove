package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.view.TicketTillView;
import com.interswitch.smartmoveserver.repository.TicketTillRepository;
import com.interswitch.smartmoveserver.repository.TicketTillSummaryRepository;
import com.interswitch.smartmoveserver.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduledTaskService {

    protected final Log logger = LogFactory.getLog(getClass());
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    TicketTillRepository ticketTillRepository;

    @Autowired
    TicketTillService ticketTillService;

    @Autowired
    TicketTillSummaryRepository ticketTillSummaryRepository;

    @Scheduled(cron = "0 * * * * ?")
    public void scheduleTaskWithCronExpression() {
        logger.info("#####Cron Task :: Execution Time - ##### "+dateTimeFormatter.format(LocalDateTime.now()));
    }

    @Scheduled(cron = "50 23 * * * ?")
    public void runEndOfDayTicketTillUpdate(){
        doEndOfDayTicketTillUpdate();
    }


    public void doEndOfDayTicketTillUpdate(){

        String todayDate = DateUtil.getTodayDate();
        List<TicketTillView> ticketTillViewSummaryList =
                ticketTillService.findAggregatedTicketTillByIssuanceDateAndStatus(todayDate,false);
        ticketTillViewSummaryList.forEach(ttv->{
            ticketTillService.closeTicketTill(ttv);
        });

    }
}
