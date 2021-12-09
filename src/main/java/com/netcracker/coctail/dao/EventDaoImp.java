package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@PropertySource("classpath:SQLscripts.properties")
public class EventDaoImp implements EventDao {

    @Value("${findEventByName}")
    private String findEventByName;
    @Value("${createEvent}")
    private String createEvent;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    RowMapper<Event> rowMapper = (rs, rownum) ->
            new Event(rs.getInt("id"),
                    rs.getString("eventname"),
                    rs.getLong("creatorid"),
                    rs.getTimestamp("eventtime"));

    @Override
    public List<Event> findEventByName(String name) {
        return jdbcTemplate.query(String.format(findEventByName, name), rowMapper);
    }

    @Override
    public void createEvent(long creatorId, CreateEvent event) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("creatorid", creatorId)
                .addValue("eventname", event.getName())
                .addValue("eventtime", event.getEventTime());
        jdbcTemplate.update(createEvent, param);
    }
}
