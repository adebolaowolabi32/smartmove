package com.interswitch.smartmoveserver.model.request;

import com.interswitch.smartmoveserver.model.User;
import lombok.Data;

import java.util.Set;

@Data
public class IswUser extends User {

    private  String domainCode;
    private  String appCode;
    private Set<IswRole> roles;

}