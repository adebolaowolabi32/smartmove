package com.interswitch.smartmoveserver.model.request;

import com.interswitch.smartmoveserver.model.Device;
import lombok.Data;

@Data
public class DeviceConnection extends Device {
    private String messageId;
}
