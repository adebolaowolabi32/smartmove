package com.interswitch.smartmoveserver.model.request;

import lombok.Data;

@Data
public class GetOwnerId {
    private String messageId;
    private String deviceId;
}
