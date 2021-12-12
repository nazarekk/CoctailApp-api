package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Event;
import com.netcracker.coctail.model.Recipe;
import com.netcracker.coctail.model.CreateEvent;
import com.netcracker.coctail.model.EventUser;

import java.util.List;

public interface EventDao {

    List<Event> findEventByName(String name);

    List<Event> findEventById(Integer id);

    void createEvent(long creatorId, CreateEvent event);

    void editEvent(long creatorId, CreateEvent event, int eventId);

    void declineEvent(int eventId);

    List<EventUser> containsUsers(int eventId);

    List<Recipe> containsRecipes(int eventId);

    List<Event> findAllEventsByName(String name);

    List<Event> getEventsFiltered(long userId);

    boolean userInEvent(Integer eventId, Long userId);

    boolean recipeInEvent(Integer eventId, Integer recipeId);

    void joinEvent(Integer eventId, Long userId);

    void addRecipeToEvent(Integer eventId, Integer recipeId);

    void leaveEvent(Integer eventId, Long userId);

    void removeRecipeFromEvent(Integer eventId, Integer recipeId);

}
