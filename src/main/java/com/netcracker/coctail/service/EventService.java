package com.netcracker.coctail.service;

import com.netcracker.coctail.model.CreateEvent;

public interface EventService {
    Integer createEvent(String ownerEmail, CreateEvent event);
}
