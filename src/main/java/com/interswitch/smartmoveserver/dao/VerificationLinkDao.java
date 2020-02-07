package com.interswitch.smartmoveserver.dao;

import com.interswitchng.passport.model.VerificationLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by Nnaemeka.Okoroafor on 08/03/2016.
 */

@Repository
public class VerificationLinkDao extends LinkDao<VerificationLink> {

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        log = new SimpleJdbcCall(jdbcTemplate).withProcedureName("log_verification_link").withReturnValue();
        find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_verification_link")
                .returningResultSet(SINGLE_RESULT, Jsr310SupportedBeanPropertyRowMapper.newInstance(VerificationLink.class));
        expire = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("expire_verification_link").withReturnValue();
        getLinkClient = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("get_client_by_verification_link")
                .returningResultSet(SINGLE_RESULT, new ClientDaoUtil());
    }
}
