package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.Route;
import lombok.Data;

import java.io.Serializable;

/*
 * Created by adebola.owolabi on 8/4/2020
 */
@Data
public class TestModel implements Serializable {
    private String routeId;
    private Route route;
}
