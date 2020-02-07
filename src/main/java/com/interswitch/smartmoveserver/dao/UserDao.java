package com.interswitch.smartmoveserver.dao;

import com.interswitchng.passport.model.Client;
import com.interswitchng.passport.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by Adesegun.Adeyemo on 31/12/2014.
 */
@Repository
public class UserDao extends AbstractDao<User> {
    private SimpleJdbcCall findByUsername, updateLogin, findUserClients, getUserPreviousPasswords, changeUserPassword;

    @Autowired
    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        create = new SimpleJdbcCall(jdbcTemplate).withProcedureName("create_user").withReturnValue();
        update = new SimpleJdbcCall(jdbcTemplate).withProcedureName("update_user").withReturnValue();
        delete = new SimpleJdbcCall(jdbcTemplate).withProcedureName("delete_user").withReturnValue();
        find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_user")
                .returningResultSet(SINGLE_RESULT, Jsr310SupportedBeanPropertyRowMapper.newInstance(User.class));
        findAll = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_users")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, Jsr310SupportedBeanPropertyRowMapper.newInstance(User.class));
        findByUsername = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_user_by_username")
                .returningResultSet(SINGLE_RESULT, Jsr310SupportedBeanPropertyRowMapper.newInstance(User.class));
        updateLogin = new SimpleJdbcCall(jdbcTemplate).withProcedureName("update_login").withReturnValue();
        findUserClients = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_user_clients")
                .returningResultSet(MULTIPLE_RESULT, new ClientDaoUtil());
        getUserPreviousPasswords = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_user_previous_passwords")
                .returningResultSet(MULTIPLE_RESULT, Jsr310SupportedBeanPropertyRowMapper.newInstance(User.class));
        changeUserPassword = new SimpleJdbcCall(jdbcTemplate).withProcedureName("change_user_password").withReturnValue();
    }

    public User find(String username) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("username", username);
        Map<String, Object> m = findByUsername.execute(in);
        List<User> result = (List<User>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User create(User user) {
        try {
            return super.create(user);

        } catch (DataAccessException ex) {
            if (ex instanceof DuplicateKeyException) {
                String message = parseDuplicateKeyError(ex.getMessage());
                if (!StringUtils.isEmpty(message)) {
                    throw new DuplicateKeyException(message);
                }
            }
            throw ex;
        }
    }

    @Override
    public void update(User user) {
        try {
            super.update(user);

        } catch (DataAccessException ex) {
            if (ex instanceof DuplicateKeyException) {
                String message = parseDuplicateKeyError(ex.getMessage());
                if (!StringUtils.isEmpty(message)) {
                    throw new DuplicateKeyException(message);
                }
            }
            throw ex;
        }
    }


    public void updateLogin(String username, Boolean passwordMatched) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("username", username).addValue("password_matched", passwordMatched);
        updateLogin.execute(in);
    }

    public List<Client> findUserClients(String username) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("username", username);
        Map<String, Object> m = findUserClients.execute(in);
        return (List<Client>) m.get(MULTIPLE_RESULT);
    }

    public List<User> getUserPreviousPasswords(String username) throws DataAccessException {
        SqlParameterSource params = new MapSqlParameterSource().addValue("username", username);
        Map<String, Object> m = getUserPreviousPasswords.execute(params);
        return (List<User>) m.get(MULTIPLE_RESULT);
    }

    public Integer changePassword(String username, String password) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("username", username).addValue("password", password);
        Map<String, Object> m = changeUserPassword.execute(in);
        return (Integer) m.get("user_id");
    }

    private static String parseDuplicateKeyError(String error) {
        String constraint = StringUtils.substringBetween(error, "unique index '", "'.");
        if (constraint == null) {
            constraint = StringUtils.substringBetween(error, "constraint '", "'.");
        }
        String message = "";
        if (constraint == null) {
            return message;
        }
        if (constraint.equals("UQ_users_email")) {
            message = "A user with the email you provided already exists";
        } else if (constraint.equals("UQ__users__username")) {
            message = "A user with the username you provided already exists";
        } else if (constraint.equals("UQ_users_mobile_no")) {
            message = "A user with the mobile number you provided already exists";
        }
        return message;
    }
}
