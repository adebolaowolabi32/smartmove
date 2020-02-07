package com.interswitch.smartmoveserver.model.response;

import lombok.Data;

@Data
public class GetOwnerIdResponse {
	private String messageId;
	private String ownerId;
	private String responseCode;
}
