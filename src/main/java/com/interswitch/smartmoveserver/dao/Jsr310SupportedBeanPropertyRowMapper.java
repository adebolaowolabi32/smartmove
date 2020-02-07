package com.interswitch.smartmoveserver.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.beans.PropertyDescriptor;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Adesegun.Adeyemo on 07/01/2015.
 */
public class Jsr310SupportedBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        Class<?> requiredType = pd.getPropertyType();
        Object value = null;
        if (LocalDateTime.class.equals(requiredType)) {
            Timestamp timestamp = rs.getTimestamp(index);
            if (timestamp != null) {
                value = timestamp.toLocalDateTime();
            }
        } else if (LocalDate.class.equals(requiredType)) {
            Date date = rs.getDate(index);
            if (date != null) {
                value = date.toLocalDate();
            }
        } else if (LocalTime.class.equals(requiredType)) {
            Time time = rs.getTime(index);
            if (time != null) {
                value = rs.getTime(index).toLocalTime();
            }
        }
        if (value == null) {
            return super.getColumnValue(rs, index, pd);
        } else {
            return (rs.wasNull() ? null : value);
        }
    }

    public static <T> Jsr310SupportedBeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
        Jsr310SupportedBeanPropertyRowMapper<T> newInstance = new Jsr310SupportedBeanPropertyRowMapper<T>();
        newInstance.setMappedClass(mappedClass);
        return newInstance;
    }
}