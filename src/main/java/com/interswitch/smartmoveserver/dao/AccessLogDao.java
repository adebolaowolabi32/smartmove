package com.interswitch.smartmoveserver.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitchng.passport.model.AccessLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by jideobi.onuora on 5/20/2015.
 */
@Repository
public class AccessLogDao {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ObjectMapper mapper = new ObjectMapper();

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall log;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        log = new SimpleJdbcCall(jdbcTemplate).withProcedureName("log_access").withReturnValue();
    }

    public void log(AccessLog accessLog) throws DataAccessException {
        SqlParameterSource in = accessLogSqlParameterSource(accessLog);
        log.execute(in);
    }

    private MapSqlParameterSource accessLogSqlParameterSource(AccessLog accessLog) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", accessLog.getUsername());
        params.addValue("client_id", accessLog.getClientId());
        params.addValue("device_name", accessLog.getDeviceName());
        params.addValue("device_type", accessLog.getDeviceType());
        params.addValue("os_name", accessLog.getOsName());
        params.addValue("browser_name", accessLog.getBrowserName());
        params.addValue("device_fingerprint", accessLog.getDeviceFingerprint());

        String ipAddressRoute = "{}";
        try {
            ipAddressRoute = mapper.writeValueAsString(accessLog.getIpAddressRoute());
        } catch (JsonProcessingException e) {
            logger.debug("Could not serialize IP address route: ", e);
        }
        params.addValue("ip_address_route", ipAddressRoute);

        return params;
    }
}
