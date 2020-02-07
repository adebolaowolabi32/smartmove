package com.interswitch.smartmoveserver.dao;

import com.interswitchng.passport.model.ResetPasswordLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by jideobi.onuora on 2/9/2015.
 */
@Repository
public class ResetPasswordLinkDao extends LinkDao<ResetPasswordLink> {

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        log = new SimpleJdbcCall(jdbcTemplate).withProcedureName("log_reset_password_link").withReturnValue();
        find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_reset_password_link")
                .returningResultSet(SINGLE_RESULT, Jsr310SupportedBeanPropertyRowMapper.newInstance(ResetPasswordLink.class));
        expire = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("expire_reset_password_link").withReturnValue();
        getLinkClient = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("get_client_by_reset_password_link")
                .returningResultSet(SINGLE_RESULT, new ClientDaoUtil());
    }
}
