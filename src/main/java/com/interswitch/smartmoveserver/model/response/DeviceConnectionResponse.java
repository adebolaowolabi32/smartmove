package com.interswitch.smartmoveserver.model.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
public class DeviceConnectionResponse {
	private String messageId;
	private String timeDate;
	private String responseCode;
}
