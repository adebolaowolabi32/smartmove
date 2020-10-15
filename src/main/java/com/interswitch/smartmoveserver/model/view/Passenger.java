package com.interswitch.smartmoveserver.model.view;

import com.interswitch.smartmoveserver.model.Enum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/*
 * Created by adebola.owolabi on 8/11/2020
 */
@Data
public class Passenger implements Serializable {

    private String name;

    private String gender;

    private String nationality;

    private Enum.IdCategory idCategory;

    private String idNumber;

    @Enumerated(EnumType.STRING)
    private Enum.SeatClass seatClass;

    private String seatNo;

}
