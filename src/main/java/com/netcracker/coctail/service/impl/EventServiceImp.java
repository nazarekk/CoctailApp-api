package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.EventDao;
import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.dao.RecipeDao;
import com.netcracker.coctail.dao.UserDao;

import com.netcracker.coctail.model.CreateEvent;
import com.netcracker.coctail.model.Event;
import com.netcracker.coctail.model.EventInfo;
import com.netcracker.coctail.model.Recipe;
import com.netcracker.coctail.service.EventService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@Data
public class EventServiceImp implements EventService {

    private final FriendlistDao friendlistDao;
    private final RecipeDao recipeDao;
    private final EventDao eventDao;
    private final UserDao userDao;

    @Override
    public List<Event> getEventsByName(String name) {
        return eventDao.findAllEventsByName(name);
    }

    @Override
    public List<Event> getEventsFiltered(String ownerEmail) {
        long userId = friendlistDao.getOwnerId(ownerEmail);
        return eventDao.getEventsFiltered(userId);
    }

    @Override
    public Integer createEvent(String ownerEmail, CreateEvent event) {
        String name = event.getName();
        long creatorId = friendlistDao.getOwnerId(ownerEmail);
        if (eventDao.findEventByName(name).isEmpty()) {
            log.info(event.getEventTime().toString());
            eventDao.createEvent(creatorId, event);
            int eventId = eventDao.findEventByName(name).get(0).getId();
            eventDao.joinEvent(eventId, creatorId);
            return eventId;
        } else {
            log.error("Event with name {} already exists", name);
            return 0;
        }
    }

    @Override
    public boolean editEvent(String ownerEmail, CreateEvent event, int eventId) {
        String name = event.getName();
        long creatorId = friendlistDao.getOwnerId(ownerEmail);
        Event result = eventDao.findEventById(eventId).get(0);
        if (result == null) {
            log.error("Event with id {} doesn't exist", eventId);
            return false;
        }
        if (result.getCreatorId() != creatorId) {
            log.error("You are not the creator of this event");
            return false;
        }
        if (eventDao.findEventByName(name).isEmpty()) {
            eventDao.editEvent(creatorId, event, eventId);
            return true;
        } else {
            log.error("Event with name {} already exists", name);
            return false;
        }
    }

    @Override
    public boolean declineEvent(String ownerEmail, int eventId) {
        long creatorId = friendlistDao.getOwnerId(ownerEmail);
        Event result = eventDao.findEventById(eventId).get(0);
        if (result == null) {
            log.error("Event with id {} doesn't exist", eventId);
            return false;
        }
        if (result.getCreatorId() != creatorId) {
            log.error("You are not the creator of this event");
            return false;
        }
        eventDao.declineEvent(eventId);
        return true;
    }

    @Override
    public EventInfo eventInfo(int id) {
        Event event = eventDao.findEventById(id).get(0);
        String nickname = userDao.findUserById(event.getCreatorId()).get(0).getNickname();
        if (event == null) {
            log.warn("IN findEventById - no events found by id: {}", id);
            return null;
        }
        int eventId = event.getId();
        return new EventInfo(
                event.getId(),
                event.getName(),
                event.getCreatorId(),
                nickname,
                event.getEventTime(),
                eventDao.containsUsers(eventId),
                eventDao.containsRecipes(eventId)
        );
    }

    @Override
    public boolean joinEvent(String ownerEmail, int eventId) {
        long userId = friendlistDao.getOwnerId(ownerEmail);
        if (eventDao.findEventById(eventId).get(0) == null) {
            log.error("Event with id {} doesn't exist", eventId);
            return false;
        }
        if (!eventDao.userInEvent(eventId, userId)) {
            eventDao.joinEvent(eventId, userId);
            return true;
        } else {
            log.error("You already participate in this event");
            return false;
        }
    }

    @Override
    public boolean addRecipeToEvent(int eventId, String name, String userEmail) {
        long userId = friendlistDao.getOwnerId(userEmail);
        if (eventDao.findEventById(eventId).get(0) == null) {
            log.error("Event with id {} doesn't exist", eventId);
            return false;
        }
        Recipe recipe = recipeDao.findRecipeByName(name).get(0);
        if (recipe == null) {
            log.error("Recipe with name {} doesn't exist", name);
            return false;
        }
        if (!eventDao.userInEvent(eventId, userId)) {
            log.error("You don't participate in this event");
            return false;
        }
        if (!eventDao.recipeInEvent(eventId, recipe.getId())) {
            eventDao.addRecipeToEvent(eventId, recipe.getId());
            return true;
        } else {
            log.error("Recipe is already added to event");
            return false;
        }
    }

    @Override
    public boolean leaveEvent(String ownerEmail, int eventId) {
        long userId = friendlistDao.getOwnerId(ownerEmail);
        if (eventDao.findEventById(eventId).get(0) == null) {
            log.error("Event with id {} doesn't exist", eventId);
            return false;
        }
        if (eventDao.userInEvent(eventId, userId)) {
            eventDao.leaveEvent(eventId, userId);
            return true;
        } else {
            log.error("You don't participate in this event");
            return false;
        }
    }

    @Override
    public boolean removeRecipeFromEvent(int eventId, String name, String userEmail) {
        long userId = friendlistDao.getOwnerId(userEmail);
        if (eventDao.findEventById(eventId).get(0) == null) {
            log.error("Event with id {} doesn't exist", eventId);
            return false;
        }
        Recipe recipe = recipeDao.findRecipeByName(name).get(0);
        if (recipe == null) {
            log.error("Recipe with name {} doesn't exist", name);
            return false;
        }
        if (!eventDao.userInEvent(eventId, userId)) {
            log.error("You don't participate in this event");
            return false;
        }
        if (eventDao.recipeInEvent(eventId, recipe.getId())) {
            eventDao.removeRecipeFromEvent(eventId, recipe.getId());
            return true;
        } else {
            log.error("There is no recipe with name {} in this event", name);
            return false;
        }
    }

}
