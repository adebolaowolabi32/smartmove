package com.interswitch.smartmoveserver.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

@Component
public class KeyValuePairDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall pspGetKeyValue, pspSetKeyValue;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        pspGetKeyValue = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_key_value");
        pspSetKeyValue = new SimpleJdbcCall(jdbcTemplate).withProcedureName("set_key_value");
    }

    public String getKeyValue(String key) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("key_label", key);
        Map<String, Object> m = pspGetKeyValue.execute(params);
        return (String) m.get("key_value");
    }

    public void setKeyValue(String key, String value) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("key_label", key).addValue("key_value", value);
        pspSetKeyValue.execute(params);
    }
}
