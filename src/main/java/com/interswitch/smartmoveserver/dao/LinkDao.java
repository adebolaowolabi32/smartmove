package com.interswitch.smartmoveserver.dao;

import com.interswitchng.passport.model.Client;
import com.interswitchng.passport.model.Link;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Jideobi.Onuora on 16/06/2016.
 *
 * @param <T>
 */
public abstract class LinkDao<T extends Link> {

    protected static final String SINGLE_RESULT = "single";
    protected JdbcTemplate jdbcTemplate;
    protected SimpleJdbcCall log, find, expire, getLinkClient;

    public abstract void setDataSource(DataSource dataSource);

    public T log(T model) throws DataAccessException {
        SqlParameterSource in = new Jsr310SupportedBeanPropertySqlParameterSource(model);
        Map<String, Object> m = log.execute(in);
        Timestamp expires_on = (Timestamp) m.get("expires_on");
        LocalDateTime expiresOn = Objects.nonNull(expires_on) ?
                expires_on.toLocalDateTime() : null;
        model.setExpiresOn(expiresOn);
        return model;
    }

    public T find(String uuid) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("uuid", uuid);
        Map<String, Object> m = find.execute(in);
        List<T> result = (List<T>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public void expire(String uuid) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("uuid", uuid);
        expire.execute(in);
    }

    public Client getLinkClient(String uuid) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("uuid", uuid);
        Map<String, Object> m = getLinkClient.execute(in);
        List<Client> result = (List<Client>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
