package com.netcracker.coctail.service;

import com.netcracker.coctail.model.CreateEvent;
import com.netcracker.coctail.model.Event;
import com.netcracker.coctail.model.EventInfo;

import java.util.List;

public interface EventService {

    Integer createEvent(String ownerEmail, CreateEvent event);

    boolean editEvent(String ownerEmail, CreateEvent event, int eventId);

    boolean declineEvent(String ownerEmail, int eventId);

    EventInfo eventInfo(int eventId);

    List<Event> getEventsByName(String name);

    List<Event> getEventsFiltered(String ownerEmail);

    boolean joinEvent(String ownerEmail, int id);

    boolean addRecipeToEvent(int id, String name, String userEmail);

    boolean leaveEvent(String ownerEmail, int id);

    boolean removeRecipeFromEvent(int eventId, String name, String userEmail);

    boolean isCreator(int id, String ownerEmail);
}
