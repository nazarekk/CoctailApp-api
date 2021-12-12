package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Event;
import com.netcracker.coctail.model.Recipe;
import com.netcracker.coctail.model.CreateEvent;
import com.netcracker.coctail.model.EventUser;
import com.netcracker.coctail.model.Alcohol;
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
    @Value("${findAllEventsByName}")
    private String findAllEventsByName;
    @Value("${getEventsFiltered}")
    private String getEventsFiltered;
    @Value("${findEventById}")
    private String findEventById;
    @Value("${createEvent}")
    private String createEvent;
    @Value("${editEvent}")
    private String editEvent;
    @Value("${declineEvent}")
    private String declineEvent;
    @Value("${joinEvent}")
    private String joinEvent;
    @Value("${addRecipeToEvent}")
    private String addRecipeToEvent;
    @Value("${leaveEvent}")
    private String leaveEvent;
    @Value("${removeRecipeFromEvent}")
    private String removeRecipeFromEvent;
    @Value("${containsUsers}")
    private String containsUsers;
    @Value("${containsRecipes}")
    private String containsRecipes;
    @Value("${userInEvent}")
    private String userInEvent;
    @Value("${recipeInEvent}")
    private String recipeInEvent;

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
    public List<Event> findAllEventsByName(String name) {
        return jdbcTemplate.query(String.format(findAllEventsByName, name + "%"), rowMapper);
    }

    @Override
    public List<Event> getEventsFiltered(long userId) {
        return jdbcTemplate.query(String.format(getEventsFiltered, userId), rowMapper);
    }

    @Override
    public List<Event> findEventById(Integer id) {
        return jdbcTemplate.query(String.format(findEventById, id), rowMapper);
    }

    @Override
    public void createEvent(long creatorId, CreateEvent event) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("creatorid", creatorId)
                .addValue("eventname", event.getName())
                .addValue("eventtime", event.getEventTime());
        jdbcTemplate.update(createEvent, param);
    }

    @Override
    public void editEvent(long creatorId, CreateEvent event, int eventId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("creatorid", creatorId)
                .addValue("eventname", event.getName())
                .addValue("eventtime", event.getEventTime())
                .addValue("id", eventId);
        jdbcTemplate.update(editEvent, param);
    }

    @Override
    public void declineEvent(int eventId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", eventId);
        jdbcTemplate.update(declineEvent, param);
    }

    @Override
    public List<EventUser> containsUsers(int eventId) {
        RowMapper<EventUser> rowMapper = (rs, rownum) ->
                new EventUser(
                        rs.getLong("userid"),
                        rs.getString("nickname"));
        return jdbcTemplate.query(String.format(containsUsers, eventId), rowMapper);
    }

    @Override
    public List<Recipe> containsRecipes(int eventId) {
        RowMapper<Recipe> rowMapper = (rs, rownum) ->
                new Recipe(rs.getInt("id"),
                        rs.getString("name"),
                        Alcohol.valueOf(rs.getString("alcohol")),
                        rs.getBoolean("sugarless"),
                        rs.getBoolean("isActive"),
                        rs.getString("image"),
                        rs.getString("recipe"),
                        rs.getInt("rating"));
        return jdbcTemplate.query(String.format(containsRecipes, eventId), rowMapper);
    }

    @Override
    public boolean userInEvent(Integer eventId, Long userId) {
        RowMapper<Integer> rowMapper = (rs, rownum) -> rs.getInt("id");
        return (!jdbcTemplate.query(
                String.format(userInEvent, eventId, userId), rowMapper).isEmpty());
    }

    @Override
    public boolean recipeInEvent(Integer eventId, Integer recipeId) {
        RowMapper<Integer> rowMapper = (rs, rownum) -> rs.getInt("id");
        return (!jdbcTemplate.query(
                String.format(recipeInEvent, eventId, recipeId), rowMapper).isEmpty());
    }

    @Override
    public void joinEvent(Integer eventId, Long userId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("eventid", eventId)
                .addValue("userid", userId);
        jdbcTemplate.update(joinEvent, param);
    }

    @Override
    public void addRecipeToEvent(Integer eventId, Integer recipeId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("eventid", eventId)
                .addValue("recipeid", recipeId);
        jdbcTemplate.update(addRecipeToEvent, param);
    }

    @Override
    public void leaveEvent(Integer eventId, Long userId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("eventid", eventId)
                .addValue("userid", userId);
        jdbcTemplate.update(leaveEvent, param);
    }

    @Override
    public void removeRecipeFromEvent(Integer eventId, Integer recipeId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("eventid", eventId)
                .addValue("recipeid", recipeId);
        jdbcTemplate.update(removeRecipeFromEvent, param);
    }

}
