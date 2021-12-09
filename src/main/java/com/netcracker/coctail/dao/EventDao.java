package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.CreateEvent;
import com.netcracker.coctail.model.Event;

import java.util.List;

public interface EventDao {

    List<Event> findEventByName(String name);

    void createEvent(long creatorId, CreateEvent event);
}
