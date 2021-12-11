package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.EventDao;
import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.dao.RecipeDao;
import com.netcracker.coctail.model.*;
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
            log.info("Event with name " + name + " already exists");
            return 0;
        }
    }

    @Override
    public boolean editEvent(String ownerEmail, CreateEvent event, int eventId) {
        String name = event.getName();
        long creatorId = friendlistDao.getOwnerId(ownerEmail);
        Event result = eventDao.findEventById(eventId).get(0);
        if (result == null) {
            log.info("Event with id " + eventId + " doesn't exist");
            return false;
        }
        if (result.getCreatorId() != creatorId) {
            log.info("You are not the creator of this event");
            return false;
        }
        if (eventDao.findEventByName(name).isEmpty()) {
            eventDao.editEvent(creatorId, event, eventId);
            return true;
        } else {
            log.info("Event with name " + name + " already exists");
            return false;
        }
    }

    @Override
    public boolean declineEvent(String ownerEmail, int eventId) {
        long creatorId = friendlistDao.getOwnerId(ownerEmail);
        Event result = eventDao.findEventById(eventId).get(0);
        if (result == null) {
            log.info("Event with id " + eventId + " doesn't exist");
            return false;
        }
        if (result.getCreatorId() != creatorId) {
            log.info("You are not the creator of this event");
            return false;
        }
        eventDao.declineEvent(eventId);
        return true;
    }

    @Override
    public EventInfo eventInfo(int id) {
        Event event = eventDao.findEventById(id).get(0);
        if (event == null) {
            log.warn("IN findEventById - no events found by id: {}", id);
            return null;
        }
        int eventId = event.getId();
        return new EventInfo(
                event.getId(),
                event.getName(),
                event.getCreatorId(),
                event.getEventTime(),
                eventDao.containsUsers(eventId),
                eventDao.containsRecipes(eventId)
        );
    }

    @Override
    public boolean joinEvent(String ownerEmail, int eventId) {
        long userId = friendlistDao.getOwnerId(ownerEmail);
        if (eventDao.findEventById(eventId).get(0) == null) {
            log.info("Event with id " + eventId + " doesn't exist");
            return false;
        }
        if (!eventDao.userInEvent(eventId, userId)) {
            eventDao.joinEvent(eventId, userId);
            return true;
        } else {
            log.info("You already participate in this event");
            return false;
        }
    }

    @Override
    public boolean addRecipeToEvent(int eventId, String name, String userEmail) {
        long userId = friendlistDao.getOwnerId(userEmail);
        if (eventDao.findEventById(eventId).get(0) == null) {
            log.info("Event with id " + eventId + " doesn't exist");
            return false;
        }
        Recipe recipe = recipeDao.findRecipeByName(name).get(0);
        if (recipe == null) {
            log.info("Recipe with name " + name + " doesn't exist");
            return false;
        }
        if (!eventDao.userInEvent(eventId, userId)) {
            log.info("You don't participate in this event");
            return false;
        }
        if (!eventDao.recipeInEvent(eventId, recipe.getId())) {
            eventDao.addRecipeToEvent(eventId, recipe.getId());
            return true;
        } else {
            log.info("Recipe is already added to event");
            return false;
        }
    }

    @Override
    public boolean leaveEvent(String ownerEmail, int eventId) {
        long userId = friendlistDao.getOwnerId(ownerEmail);
        if (eventDao.findEventById(eventId).get(0) == null) {
            log.info("Event with id " + eventId + " doesn't exist");
            return false;
        }
        if (eventDao.userInEvent(eventId, userId)) {
            eventDao.leaveEvent(eventId, userId);
            return true;
        } else {
            log.info("You don't participate in this event");
            return false;
        }
    }

    @Override
    public boolean removeRecipeFromEvent(int eventId, String name, String userEmail) {
        long userId = friendlistDao.getOwnerId(userEmail);
        if (eventDao.findEventById(eventId).get(0) == null) {
            log.info("Event with id " + eventId + " doesn't exist");
            return false;
        }
        Recipe recipe = recipeDao.findRecipeByName(name).get(0);
        if (recipe == null) {
            log.info("Recipe with name " + name + " doesn't exist");
            return false;
        }
        if (!eventDao.userInEvent(eventId, userId)) {
            log.info("You don't participate in this event");
            return false;
        }
        if (eventDao.recipeInEvent(eventId, recipe.getId())) {
            eventDao.removeRecipeFromEvent(eventId, recipe.getId());
            return true;
        } else {
            log.info("There is no recipe with name " + name + " in this event");
            return false;
        }
    }

}
