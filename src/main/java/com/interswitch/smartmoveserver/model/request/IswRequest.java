package com.interswitch.smartmoveserver.model.request;

import lombok.Data;

@Data
public class IswRequest {
    private  String username;
    private  String domainCode;
    private  String appCode;
}