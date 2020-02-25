package com.interswitch.smartmoveserver.service;

public class SystemTimingsServiceTests {

/*    @Autowired
    private ConfigService configService;

    @MockBean
    private ConfigRepository configRepository;

    private Config config1;
    private Config config2;
    private SystemTimings systemTimings;
    private GetSystemTimings getTimings;
    private GetSystemTimingsResponse getTimingsResponse;

    @Before
    public void setup() {
        systemTimings = new SystemTimings();
        systemTimings.setPeriodGPS("12");
        systemTimings.setPeriodTransactionUpload("15");
        getTimings = new GetSystemTimings();
        getTimings.setDeviceId("id_beval");
        getTimings.setMessageId("id_message");
        getTimingsResponse = new GetSystemTimingsResponse();
        getTimingsResponse.setMessageId("id_message");
        getTimingsResponse.setPeriodGPS("12");
        getTimingsResponse.setPeriodTransactionUpload("15");
        getTimingsResponse.setResponseCode("00");
        config1 = new Config();
        config1.setName("periodTransactionUpload");
        config1.setValue(systemTimings.getPeriodTransactionUpload());
        config2 = new Config();
        config2.setName("periodGPS");
        config2.setValue(systemTimings.getPeriodGPS());
    }

    @Test
    public void testSaveTimings() {
        when(configRepository.save(config1)).thenReturn(config1);
        when(configRepository.save(config2)).thenReturn(config2);
        assertThat(configService.saveSystemTimings(systemTimings).getPeriodGPS()).isEqualTo(systemTimings.getPeriodGPS());
    }

    @Test
    public void testGetSystemTimings() {
        when(configRepository.findAll()).thenReturn(Arrays.asList(config1, config2));
        assertThat(configService.getSystemTimings(getTimings).getPeriodGPS()).isEqualTo(getTimingsResponse.getPeriodGPS());
    }*/
}
