package com.interswitch.smartmoveserver.dao;

import com.interswitch.smartmoveserver.model.AbstractModel;
import com.interswitch.smartmoveserver.model.Page;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<T extends AbstractModel> {

    protected static final String SINGLE_RESULT = "single";
    protected static final String MULTIPLE_RESULT = "list";
    protected static final String RESULT_COUNT = "count";
    protected JdbcTemplate jdbcTemplate;
    protected SimpleJdbcCall create, update, delete, find, findAll;

    public abstract void setDataSource(DataSource dataSource);

    public T create(T model) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(model);
        Map<String, Object> m = create.execute(in);
        int id = (Integer) m.get("id");
        model.setId(id);
        return model;
    }

    public void update(T model) throws DataAccessException {
        SqlParameterSource in = new BeanPropertySqlParameterSource(model);
        update.execute(in);
    }

    public void delete(Integer id) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("id", id);
        delete.execute(in);
    }

    public T find(Integer id) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("id", id);
        Map<String, Object> m = find.execute(in);
        List<T> result = (List<T>) m.get(SINGLE_RESULT);
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public List<T> findAll() throws DataAccessException {
        return findAll(0, 0).getContent();
    }

    public Page<T> findAll(Integer pageNum, Integer pageSize) throws DataAccessException {
        SqlParameterSource in = new MapSqlParameterSource().addValue("page_num", pageNum).addValue("page_size", pageSize == 0 ? null : pageSize);
        Map<String, Object> m = findAll.execute(in);
        List<T> content = (List<T>) m.get(MULTIPLE_RESULT);
        Long count = ((List<Long>) m.get(RESULT_COUNT)).get(0);
        Page<T> page = new Page<>(count, content);
        return page;
    }

    public class RowCountMapper implements RowMapper {
        @Override
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong(1);
        }
    }
}
