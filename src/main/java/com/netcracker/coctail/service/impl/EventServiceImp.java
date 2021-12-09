package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.EventDao;
import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.model.CreateEvent;
import com.netcracker.coctail.service.EventService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@Data
public class EventServiceImp implements EventService {

    private final FriendlistDao friendlistDao;
    private final EventDao eventDao;

    @Override
    public Integer createEvent(String ownerEmail, CreateEvent event) {
        String name = event.getName();
        long creatorId = friendlistDao.getOwnerId(ownerEmail);
        if (eventDao.findEventByName(name).isEmpty()) {
            log.info(event.getEventTime().toString());
            eventDao.createEvent(creatorId, event);
            return eventDao.findEventByName(name).get(0).getId();
        } else {
            log.info("Event with name " + name + " already exists");
            return 0;
        }
    }

}
