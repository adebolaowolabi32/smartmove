package com.interswitch.smartmoveserver.model.request;

import com.interswitch.smartmoveserver.model.Enum;
import lombok.Data;

@Data
public class IswRole {

    private  int domainId;
    private  int appId;
    private  String name;


    public void setName(Enum.Role role) {
    }
}