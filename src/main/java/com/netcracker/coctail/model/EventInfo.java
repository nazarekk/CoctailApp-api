package com.netcracker.coctail.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class EventInfo {
    private final Integer id;
    private final String name;
    private final Long creatorId;
    private final String nickname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Timestamp eventTime;
    private final List<EventUser> userList;
    private final List<Recipe> recipeList;
}
