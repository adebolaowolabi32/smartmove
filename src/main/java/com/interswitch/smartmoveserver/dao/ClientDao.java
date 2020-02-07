package com.interswitch.smartmoveserver.dao;

import com.interswitchng.passport.api.model.QueryRequest;
import com.interswitchng.passport.model.Client;
import com.interswitchng.passport.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by Jideobi.Onuora on 1/15/2015.
 */

@Repository
public class ClientDao extends AbstractDao<Client> {
    SimpleJdbcCall updateClientSecret, findByClientName, findByClientNameOrClientId;

    @Autowired
    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        create = new SimpleJdbcCall(jdbcTemplate).withProcedureName("create_client").withReturnValue();
        update = new SimpleJdbcCall(jdbcTemplate).withProcedureName("update_client").withReturnValue();
        delete = new SimpleJdbcCall(jdbcTemplate).withProcedureName("delete_client").withReturnValue();
        find = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_client")
                .returningResultSet(SINGLE_RESULT, new ClientDaoUtil());
        findByClientName = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_client_by_name")
                .returningResultSet(SINGLE_RESULT, new ClientDaoUtil());
        findAll = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_clients")
                .returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, new ClientDaoUtil());
        updateClientSecret = new SimpleJdbcCall(jdbcTemplate).withProcedureName("update_client_secret")
                .withReturnValue();
        findByClientNameOrClientId = new SimpleJdbcCall(jdbcTemplate).withProcedureName("get_clients_by_client_id_or_client_name").returningResultSet(RESULT_COUNT, new RowCountMapper())
                .returningResultSet(MULTIPLE_RESULT, new ClientDaoUtil());
    }

    @Override
    public Client create(Client client) throws DataAccessException {
        SqlParameterSource in = new ClientDaoUtil().getClientPropertyMapSqlParameterSource(client);
        Map<String, Object> m = create.execute(in);
        int id = (Integer) m.get("id");
        client.setId(id);
        return client;
    }

    @Override
    public void update(Client client) throws DataAccessException {
        SqlParameterSource in = new ClientDaoUtil().getClientPropertyMapSqlParameterSourceForUpdate(client);
        update.execute(in);
    }

    public void delete(String clientId) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("client_id", clientId);
        delete.execute(in);
    }

    public Client find(String clientId) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("client_id", clientId);
        Map<String, Object> m = find.execute(in);
        List<Client> result = (List<Client>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public Client findByClientName(String clientName) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("client_name", clientName);
        Map<String, Object> m = findByClientName.execute(in);
        List<Client> result = (List<Client>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public Page<Client> findByClientNameOrClientId(QueryRequest queryRequest) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("search_value", queryRequest.getSearchValue()).addValue("page_num", queryRequest.getPageNum()).addValue("page_size", queryRequest.getPageSize() == 0 ? null : queryRequest.getPageSize());
        Map<String, Object> m = findByClientNameOrClientId.execute(in);
        List<Client> content = (List<Client>) m.get(MULTIPLE_RESULT);
        Long count = ((List<Long>) m.get(RESULT_COUNT)).get(0);
        Page<Client> page = new Page<>(count, content);
        return page;
    }

    public void updateClientSecret(String clientId, String clientSecret, String clientSecretEnc) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("client_id", clientId)
                .addValue("client_secret", clientSecret)
                .addValue("client_secret_enc", clientSecretEnc);

        updateClientSecret.execute(in);
    }
}
