package com.interswitch.smartmoveserver.model.response;

import lombok.Data;

@Data
public class GetDeviceIdResponse {
	private String messageId;
	private String deviceId;
	private String responseCode;
}
