package com.interswitch.smartmoveserver.model.response;

import com.interswitch.smartmoveserver.model.SystemTimings;
import lombok.Data;

@Data
public class GetSystemTimingsResponse extends SystemTimings {
   private String messageId;
    private String responseCode;
}
