package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.DeviceConnection;
import com.interswitch.smartmoveserver.model.request.GetDeviceId;
import com.interswitch.smartmoveserver.model.request.GetOwnerId;
import com.interswitch.smartmoveserver.model.response.DeviceConnectionResponse;
import com.interswitch.smartmoveserver.model.response.GetDeviceIdResponse;
import com.interswitch.smartmoveserver.model.response.GetOwnerIdResponse;
import com.interswitch.smartmoveserver.repository.DeviceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceServiceTests {
    @Autowired
    private DeviceService deviceService;
    @MockBean
    private DeviceRepository deviceRepository;

    private Device device;
    private GetDeviceId getDeviceId;
    private GetDeviceIdResponse getDeviceIdResponse;
    private GetOwnerId getOwnerId;
    private GetOwnerIdResponse getOwnerIdResponse;
    private DeviceConnection deviceConnection;
    private DeviceConnectionResponse deviceConnectionResponse;


    @Before
    public void setup() {
        device = new Device();
        device.setOwner(new User());
        device.setDeviceId("id_device");
        deviceConnection = new DeviceConnection();
        deviceConnection.setOwner(new User());
        deviceConnection.setDeviceId("id_device");
        deviceConnection.setMessageId("id_message");
        deviceConnectionResponse = new DeviceConnectionResponse();
        deviceConnectionResponse.setResponseCode("00");
        deviceConnectionResponse.setMessageId("id_message");
        getOwnerId = new GetOwnerId();
        getOwnerId.setDeviceId("id_device");
        getOwnerId.setMessageId("id_message");
        getOwnerIdResponse = new GetOwnerIdResponse();
        getOwnerIdResponse.setResponseCode("00");
        getOwnerIdResponse.setMessageId("id_message");
        getDeviceId = new GetDeviceId();
        getDeviceId.setMessageId("id_message");
        getDeviceIdResponse = new GetDeviceIdResponse();
        getDeviceIdResponse.setResponseCode("00");
        getDeviceIdResponse.setMessageId("id_message");
    }

    @Test
    public void testConnectDevice() {
        when(deviceRepository.save(device)).thenReturn(device);
        assertThat(deviceService.connectDevice(deviceConnection).getMessageId()).isEqualTo(deviceConnectionResponse.getMessageId());
    }

    @Test
    public void testGetDeviceID() {
        assertThat(deviceService.getDeviceId(getDeviceId).getMessageId()).isEqualTo(getDeviceIdResponse.getMessageId());
    }
}
