package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.model.Ticket;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 * Created by adebola.owolabi on 9/21/2020
 */
@Data
public class ReassignTicket implements Serializable {
    private String referenceNo;
    private Ticket ticket;
    private Schedule schedule;
    private List<Schedule> schedules;

}
