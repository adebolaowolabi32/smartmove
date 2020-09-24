package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.Manifest;
import com.interswitch.smartmoveserver.model.Schedule;
import lombok.Data;

import java.io.Serializable;

/*
 * Created by adebola.owolabi on 9/23/2020
 */
@Data
public class Shuffle implements Serializable {
    private Schedule schedule;
    private String manifestId;
}
